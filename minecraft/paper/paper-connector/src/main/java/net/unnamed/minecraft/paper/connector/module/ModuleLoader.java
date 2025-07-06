package net.unnamed.minecraft.paper.connector.module;

import net.unnamed.minecraft.paper.connector.PaperConnectorPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class ModuleLoader {
    private static final Logger LOGGER = Logger.getLogger("ModuleLoader");

    private final PaperConnectorPlugin plugin;
    private final ModuleManager moduleManager;
    private final Map<String, ModuleDescriptor> moduleDescriptors = new HashMap<>();
    private final Map<String, ClassLoader> moduleClassLoaders = new HashMap<>();

    public ModuleLoader(PaperConnectorPlugin plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
    }

    /**
     * Loads modules from the specified directory
     */
    public void loadModules(File modulesDirectory) {
        if (!modulesDirectory.exists() || !modulesDirectory.isDirectory()) {
            LOGGER.warning("Modules directory does not exist: " + modulesDirectory.getAbsolutePath());
            return;
        }

        File[] moduleFiles = modulesDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (moduleFiles == null) {
            LOGGER.info("No module files found in directory: " + modulesDirectory.getAbsolutePath());
            return;
        }

        // First pass: discover all modules
        for (File moduleFile : moduleFiles) {
            try {
                discoverModule(moduleFile);
            } catch (Exception e) {
                LOGGER.severe("Failed to discover module from " + moduleFile.getName() + ": " + e.getMessage());
            }
        }

        // Second pass: load modules in dependency order
        List<ModuleDescriptor> sortedModules = sortModulesByDependencies();
        for (ModuleDescriptor descriptor : sortedModules) {
            try {
                loadModule(descriptor);
            } catch (Exception e) {
                LOGGER.severe("Failed to load module " + descriptor.getId() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Reloads a specific module by its ID
     */
    public void reloadModule(String moduleId) {
        try {
            ModuleDescriptor descriptor = moduleDescriptors.get(moduleId);
            if (descriptor == null) {
                LOGGER.warning("Cannot reload module " + moduleId + ": module not found");
                return;
            }

            LOGGER.info("Reloading module: " + moduleId);

            // Unload the module
            unloadModule(moduleId);

            // Re-discover and load the module
            discoverModule(descriptor.getJarFile());
            ModuleDescriptor newDescriptor = moduleDescriptors.get(moduleId);
            if (newDescriptor != null) {
                loadModule(newDescriptor);
                LOGGER.info("Successfully reloaded module: " + moduleId);
            } else {
                LOGGER.severe("Failed to rediscover module after reload: " + moduleId);
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to reload module " + moduleId + ": " + e.getMessage());
        }
    }

    /**
     * Hot-swaps a module with a new JAR file
     */
    public void hotSwapModule(String moduleId, File newJarFile) {
        try {
            LOGGER.info("Hot-swapping module: " + moduleId);

            // Unload the existing module
            unloadModule(moduleId);

            // Load the new version
            discoverModule(newJarFile);
            ModuleDescriptor newDescriptor = moduleDescriptors.get(moduleId);
            if (newDescriptor != null) {
                loadModule(newDescriptor);
                LOGGER.info("Successfully hot-swapped module: " + moduleId);
            } else {
                LOGGER.severe("Failed to load new version of module: " + moduleId);
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to hot-swap module " + moduleId + ": " + e.getMessage());
        }
    }

    /**
     * Discovers a module from a JAR file
     */
    private void discoverModule(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry configEntry = jar.getJarEntry("module.yml");
            if (configEntry == null) {
                LOGGER.warning("Module " + jarFile.getName() + " missing module.yml");
                return;
            }

            // Parse module configuration
            ModuleDescriptor descriptor = parseModuleConfig(jar, configEntry, jarFile);
            if (descriptor != null) {
                moduleDescriptors.put(descriptor.getId(), descriptor);
                LOGGER.info("Discovered module: " + descriptor.getId() + " v" + descriptor.getVersion());
            }
        }
    }

    /**
     * Parses module configuration from module.yml
     */
    private ModuleDescriptor parseModuleConfig(JarFile jar, JarEntry configEntry, File jarFile) {
        try (var inputStream = jar.getInputStream(configEntry)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            String id = properties.getProperty("id");
            String mainClass = properties.getProperty("main");
            String version = properties.getProperty("version", "1.0.0");
            String[] dependencies = properties.getProperty("dependencies", "").split(",");

            if (id == null || mainClass == null) {
                LOGGER.warning("Module " + jarFile.getName() + " has invalid configuration");
                return null;
            }

            return new ModuleDescriptor(id, mainClass, version,
                    Arrays.stream(dependencies)
                            .map(String::trim)
                            .filter(dep -> !dep.isEmpty())
                            .toArray(String[]::new),
                    jarFile);
        } catch (IOException e) {
            LOGGER.severe("Failed to parse module config for " + jarFile.getName() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Sorts modules by their dependencies using topological sort
     */
    private List<ModuleDescriptor> sortModulesByDependencies() {
        List<ModuleDescriptor> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (ModuleDescriptor descriptor : moduleDescriptors.values()) {
            if (!visited.contains(descriptor.getId())) {
                if (visitModule(descriptor, visited, visiting, result)) {
                    LOGGER.severe("Circular dependency detected involving module: " + descriptor.getId());
                }
            }
        }

        return result;
    }

    private boolean visitModule(ModuleDescriptor descriptor, Set<String> visited,
                                Set<String> visiting, List<ModuleDescriptor> result) {
        if (visiting.contains(descriptor.getId())) {
            return true; // Circular dependency
        }

        if (visited.contains(descriptor.getId())) {
            return false;
        }

        visiting.add(descriptor.getId());

        // Visit dependencies first
        for (String dependency : descriptor.getDependencies()) {
            ModuleDescriptor depDescriptor = moduleDescriptors.get(dependency);
            if (depDescriptor != null) {
                if (visitModule(depDescriptor, visited, visiting, result)) {
                    return true;
                }
            } else {
                LOGGER.warning("Module " + descriptor.getId() + " depends on missing module: " + dependency);
            }
        }

        visiting.remove(descriptor.getId());
        visited.add(descriptor.getId());
        result.add(descriptor);

        return false;
    }

    /**
     * Loads a module from its descriptor
     */
    private void loadModule(ModuleDescriptor descriptor) throws Exception {
        // Create class loader for the module
        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{descriptor.getJarFile().toURI().toURL()},
                this.getClass().getClassLoader()
        );

        moduleClassLoaders.put(descriptor.getId(), classLoader);

        // Load the main class
        Class<?> moduleClass = classLoader.loadClass(descriptor.getMainClass());

        // Verify it implements Module interface
        if (!Module.class.isAssignableFrom(moduleClass)) {
            throw new IllegalArgumentException("Module class must implement Module interface");
        }

        // Create instance
        Constructor<?> constructor = moduleClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Module module = (Module) constructor.newInstance();

        // Configure the module if it's a ConnectorModule
        if (module instanceof ConnectorModule) {
            ConnectorModule connectorModule = (ConnectorModule) module;
            connectorModule.setPlugin(plugin);

            // Create and set module config
            ModuleConfig config = createModuleConfig(descriptor);
            connectorModule.setConfig(config);
        }

        // Register the module (but don't enable it yet)
        moduleManager.registerModule(module);

        // Enable the module
        moduleManager.enableModule(descriptor.getId());

        LOGGER.info("Successfully loaded module: " + descriptor.getId() + " v" + descriptor.getVersion());
    }

    /**
     * Creates a module config from the descriptor
     */
    private ModuleConfig createModuleConfig(ModuleDescriptor descriptor) {
        return new ModuleConfig() {
            @Override
            public String getId() {
                return descriptor.getId();
            }

            @Override
            public String getVersion() {
                return descriptor.getVersion();
            }

            @Override
            public String[] getDependencies() {
                return descriptor.getDependencies();
            }
        };
    }

    /**
     * Unloads all modules
     */
    public void unloadAllModules() {
        for (String moduleId : new ArrayList<>(moduleDescriptors.keySet())) {
            try {
                unloadModule(moduleId);
            } catch (Exception e) {
                LOGGER.severe("Failed to unload module " + moduleId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Unloads a specific module
     */
    public void unloadModule(String moduleId) throws Exception {
        ModuleDescriptor descriptor = moduleDescriptors.get(moduleId);
        if (descriptor == null) {
            LOGGER.warning("Module not found: " + moduleId);
            return;
        }

        // Unregister the module (this will also disable it)
        moduleManager.unregisterModule(moduleId);

        // Close the class loader to release resources
        ClassLoader classLoader = moduleClassLoaders.remove(moduleId);
        if (classLoader instanceof URLClassLoader) {
            ((URLClassLoader) classLoader).close();
        }

        moduleDescriptors.remove(moduleId);
        LOGGER.info("Unloaded module: " + moduleId);
    }

    /**
     * Gets all loaded module descriptors
     */
    public Collection<ModuleDescriptor> getLoadedModules() {
        return Collections.unmodifiableCollection(moduleDescriptors.values());
    }

    /**
     * Gets module descriptor by ID
     */
    public ModuleDescriptor getModuleDescriptor(String moduleId) {
        return moduleDescriptors.get(moduleId);
    }

}