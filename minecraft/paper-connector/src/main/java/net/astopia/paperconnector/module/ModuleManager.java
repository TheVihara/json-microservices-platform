package net.astopia.paperconnector.module;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.paperconnector.PaperConnectorPlugin;
import net.astopia.paperconnector.api.module.Module;
import net.astopia.paperconnector.api.module.ModuleApi;
import net.astopia.paperconnector.api.module.ModuleLoader;
import net.astopia.paperconnector.api.module.ModuleState;
import net.astopia.paperconnector.command.ModuleCommand;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleManager implements ModuleApi {
    static Logger LOGGER = Logger.getLogger("ModuleManager");
    HashMap<String, Module> modules = new HashMap<>();
    HashMap<String, ModuleState> moduleStates = new HashMap<>();
    ModuleLoader moduleLoader;

    public ModuleManager(PaperConnectorPlugin plugin) {
        this.moduleLoader = new ModuleLoader(plugin, this);
    }

    public void init(PaperConnectorPlugin plugin) {
        File modulesDir = new File(plugin.getDataFolder(), "modules");
        if (!modulesDir.exists()) {
            modulesDir.mkdirs();
        }

        moduleLoader.loadModules(modulesDir);
    }

    public void enable(PaperConnectorPlugin plugin) {
        plugin.registerCommand("module", new ModuleCommand(this, moduleLoader));
    }

    @Override
    public void registerModule(Module module) {
        String id = getModuleId(module);
        modules.put(id, module);
        moduleStates.put(id, ModuleState.DISABLED);
        LOGGER.info("Registered module: " + id);
    }

    @Override
    public void enableModule(String id) {
        Module module = modules.get(id);
        if (module == null) {
            LOGGER.warning("Cannot enable module " + id + ": module not found");
            return;
        }

        ModuleState currentState = moduleStates.get(id);
        if (currentState == ModuleState.ENABLED) {
            LOGGER.info("Module " + id + " is already enabled");
            return;
        }

        try {
            module.onEnable();
            moduleStates.put(id, ModuleState.ENABLED);
            LOGGER.info("Enabled module: " + id);
        } catch (Exception e) {
            moduleStates.put(id, ModuleState.ERROR);
            LOGGER.severe("Failed to enable module " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to enable module " + id, e);
        }
    }

    @Override
    public void disableModule(String id) {
        net.astopia.paperconnector.api.module.Module module = modules.get(id);
        if (module == null) {
            LOGGER.warning("Cannot disable module " + id + ": module not found");
            return;
        }

        ModuleState currentState = moduleStates.get(id);
        if (currentState == ModuleState.DISABLED) {
            LOGGER.info("Module " + id + " is already disabled");
            return;
        }

        try {
            module.onDisable();
            moduleStates.put(id, ModuleState.DISABLED);
            LOGGER.info("Disabled module: " + id);
        } catch (Exception e) {
            moduleStates.put(id, ModuleState.ERROR);
            LOGGER.severe("Failed to disable module " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to disable module " + id, e);
        }
    }

    @Override
    public void restartModule(String id) {
        LOGGER.info("Restarting module: " + id);
        disableModule(id);
        enableModule(id);
    }

    @Override
    public void unregisterModule(String id) {
        Module module = modules.get(id);
        if (module == null) {
            LOGGER.warning("Cannot unregister module " + id + ": module not found");
            return;
        }

        // Disable the module first if it's enabled
        if (moduleStates.get(id) == ModuleState.ENABLED) {
            disableModule(id);
        }

        modules.remove(id);
        moduleStates.remove(id);
        LOGGER.info("Unregistered module: " + id);
    }

    public Module getModule(String id) {
        return modules.get(id);
    }

    public Collection<Module> getAllModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public boolean isModuleLoaded(String id) {
        return modules.containsKey(id);
    }

    public boolean isModuleEnabled(String id) {
        return moduleStates.get(id) == ModuleState.ENABLED;
    }

    public ModuleState getModuleState(String id) {
        return moduleStates.getOrDefault(id, ModuleState.DISABLED);
    }

    public void disableAllModules() {
        for (String id : modules.keySet()) {
            try {
                disableModule(id);
            } catch (Exception e) {
                LOGGER.severe("Error disabling module " + id + ": " + e.getMessage());
            }
        }
    }

    public void enableAllModules() {
        for (String id : modules.keySet()) {
            try {
                enableModule(id);
            } catch (Exception e) {
                LOGGER.severe("Error enabling module " + id + ": " + e.getMessage());
            }
        }
    }

    private String getModuleId(Module module) {
        // Try to get ID from config first, fallback to class name
        if (module.getConfig() != null && module.getConfig().getId() != null) {
            return module.getConfig().getId();
        }
        return module.getClass().getName();
    }
}
