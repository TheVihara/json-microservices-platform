package net.unnamed.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class YamlConfig<T extends YamlConfig<T>> extends ConfigBase {

    @JsonIgnore
    Class<T> selfClazz;

    @JsonIgnore
    File file;

    @Override
    public void reload() {
        try {
            T loaded = load(selfClazz, file.toPath(), () -> {
                try {
                    return selfClazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create default config instance", e);
                }
            });

            // Copy all non-transient, non-final fields from loaded config to this instance
            for (Field field : selfClazz.getDeclaredFields()) {
                int mod = field.getModifiers();

                if (Modifier.isTransient(mod) || Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(this, field.get(loaded));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to reload config from " + file, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update config fields", e);
        }
    }

    @Override
    public void save() {
        if (file == null) {
            throw new IllegalStateException("Cannot save resource-only config. Use loadFromResource() instead of loadResourceOnly() if you need save functionality.");
        }

        try {
            Files.createDirectories(file.toPath().getParent());
            MAPPER.writeValue(file, this);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config to " + file, e);
        }
    }

    private static final String EXT = ".yml";
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

    public static <T extends YamlConfig<T>> T load(Class<T> clazz, Path path, Supplier<T> defaultConfig) throws IOException {
        if (!Files.exists(path)) {
            T suppliedConfig = defaultConfig.get();

            // Ensure parent directories exist
            Files.createDirectories(path.getParent());

            // Write default config to file
            MAPPER.writeValue(path.toFile(), suppliedConfig);

            // Set the file reference and self class
            suppliedConfig.setFile(path.toFile());
            suppliedConfig.setSelfClazz(clazz);

            return suppliedConfig;
        } else {
            T config = MAPPER.readValue(path.toFile(), clazz);
            config.setFile(path.toFile());
            config.setSelfClazz(clazz);
            return config;
        }
    }

    // Convenience method for loading without explicit exception handling
    public static <T extends YamlConfig<T>> T loadSafe(Class<T> clazz, Path path, Supplier<T> defaultConfig) {
        try {
            return load(clazz, path, defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from " + path, e);
        }
    }

    public static <T extends YamlConfig<T>> T loadFromResource(Class<T> clazz, String resourcePath, Path filePath, Supplier<T> defaultConfig) throws IOException {
        if (Files.exists(filePath)) {
            T config = MAPPER.readValue(filePath.toFile(), clazz);
            config.setFile(filePath.toFile());
            config.setSelfClazz(clazz);
            return config;
        }

        InputStream resourceStream = clazz.getResourceAsStream(resourcePath);
        if (resourceStream != null) {
            try {
                T config = MAPPER.readValue(resourceStream, clazz);

                Files.createDirectories(filePath.getParent());
                MAPPER.writeValue(filePath.toFile(), config);

                config.setFile(filePath.toFile());
                config.setSelfClazz(clazz);
                return config;
            } finally {
                resourceStream.close();
            }
        }

        T suppliedConfig = defaultConfig.get();
        Files.createDirectories(filePath.getParent());
        MAPPER.writeValue(filePath.toFile(), suppliedConfig);

        suppliedConfig.setFile(filePath.toFile());
        suppliedConfig.setSelfClazz(clazz);
        return suppliedConfig;
    }

    public static <T extends YamlConfig<T>> T loadFromResourceSafe(Class<T> clazz, String resourcePath, Path filePath, Supplier<T> defaultConfig) {
        try {
            return loadFromResource(clazz, resourcePath, filePath, defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from resource " + resourcePath + " or file " + filePath, e);
        }
    }

    public static <T extends YamlConfig<T>> T loadResourceOnly(Class<T> clazz, String resourcePath) throws IOException {
        InputStream resourceStream = clazz.getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        try {
            T config = MAPPER.readValue(resourceStream, clazz);
            config.setSelfClazz(clazz);
            return config;
        } finally {
            resourceStream.close();
        }
    }

    public static <T extends YamlConfig<T>> T loadResourceOnlySafe(Class<T> clazz, String resourcePath) {
        try {
            return loadResourceOnly(clazz, resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from resource " + resourcePath, e);
        }
    }
}