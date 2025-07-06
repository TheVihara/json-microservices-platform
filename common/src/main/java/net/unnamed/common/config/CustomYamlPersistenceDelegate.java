package net.unnamed.common.config;

import de.bsommerfeld.jshepherd.annotation.Comment;
import de.bsommerfeld.jshepherd.annotation.CommentSection;
import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.AbstractPersistenceDelegate;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import de.bsommerfeld.jshepherd.utils.ClassUtils;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

class CustomYamlPersistenceDelegate<T extends ConfigurablePojo<T>>
        extends AbstractPersistenceDelegate<T> {
    // Lazy initialization - will be set on first use
    private Yaml yaml; // For loading the whole POJO and for simple dump
    private Yaml valueDumper; // For dumping individual field values in complex save

    CustomYamlPersistenceDelegate(Path filePath, boolean useComplexSaveWithComments) {
        super(filePath, useComplexSaveWithComments);
    }

    private void initializeYamlIfNeeded(Class<T> pojoClass) {
        if (this.yaml != null) {
            return; // Already initialized
        }

        // Main Yaml instance configuration
        DumperOptions mainDumperOptions = new DumperOptions();
        mainDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        mainDumperOptions.setPrettyFlow(true);
        mainDumperOptions.setIndent(2);
        mainDumperOptions.setIndicatorIndent(1);
        mainDumperOptions.setSplitLines(false);
        mainDumperOptions.setAllowUnicode(true);
        mainDumperOptions.setExplicitStart(false);
        mainDumperOptions.setExplicitEnd(false);

        Representer representer = new Representer(mainDumperOptions);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        this.yaml = new Yaml(new Constructor(pojoClass, loaderOptions), representer, mainDumperOptions);

        // Yaml instance for dumping individual values
        DumperOptions valueDumperOptions = new DumperOptions();
        valueDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        valueDumperOptions.setIndent(2);
        valueDumperOptions.setIndicatorIndent(1);
        valueDumperOptions.setSplitLines(false);
        valueDumperOptions.setAllowUnicode(true);
        valueDumperOptions.setExplicitStart(false);
        valueDumperOptions.setExplicitEnd(false);
        this.valueDumper = new Yaml(new Representer(valueDumperOptions), valueDumperOptions);
    }

    @Override
    protected boolean tryLoadFromFile(T instance) throws Exception {
        // Initialize YAML with the actual POJO class
        initializeYamlIfNeeded((Class<T>) instance.getClass());

        try (Reader reader = Files.newBufferedReader(filePath)) {
            Yaml simpleYaml = new Yaml();
            Object yamlData = simpleYaml.load(reader);

            if (yamlData != null) {
                applyDataToInstance(instance, new YamlDataExtractor(yamlData));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveSimple(T pojoInstance, Path targetPath) throws IOException {
        // Ensure YAML is initialized before saving
        initializeYamlIfNeeded((Class<T>) pojoInstance.getClass());

        try (Writer writer =
                     Files.newBufferedWriter(
                             targetPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            Comment classComment = pojoInstance.getClass().getAnnotation(Comment.class);
            if (classComment != null && classComment.value().length > 0) {
                for (String line : classComment.value()) {
                    writer.write("# " + line + System.lineSeparator());
                }
                writer.write(System.lineSeparator());
            }
            yaml.dump(pojoInstance, writer);
        }
    }

    @Override
    protected void saveWithComments(T pojoInstance, Path targetPath) throws IOException {
        // Ensure YAML is initialized before saving
        initializeYamlIfNeeded((Class<T>) pojoInstance.getClass());

        try (PrintWriter writer =
                     new PrintWriter(
                             Files.newBufferedWriter(
                                     targetPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            this.lastCommentSectionHash = null;

            Comment classComment = pojoInstance.getClass().getAnnotation(Comment.class);
            if (classComment != null && classComment.value().length > 0) {
                for (String line : classComment.value()) writer.println("# " + line);
                writer.println();
            }

            saveFieldsWithComments(pojoInstance, writer, 0);
        }
    }

    /**
     * Recursively save fields with comments, supporting nested ConfigurablePojo objects
     */
    private void saveFieldsWithComments(Object instance, PrintWriter writer, int indentLevel) throws IOException {
        String indent = "  ".repeat(indentLevel);

        List<Field> fields = ClassUtils.getAllFieldsInHierarchy(instance.getClass(), ConfigurablePojo.class);

        for (int fieldIdx = 0; fieldIdx < fields.size(); fieldIdx++) {
            Field field = fields.get(fieldIdx);
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            Key keyAnnotation = field.getAnnotation(Key.class);
            if (keyAnnotation == null) continue;

            String yamlKey = keyAnnotation.value().isEmpty() ? field.getName() : keyAnnotation.value();

            // Handle comment sections (only at root level)
            if (indentLevel == 0) {
                CommentSection sectionAnnotation = field.getAnnotation(CommentSection.class);
                if (sectionAnnotation != null && sectionAnnotation.value().length > 0) {
                    String currentSectionHash = String.join("|", sectionAnnotation.value());
                    if (!currentSectionHash.equals(this.lastCommentSectionHash)) {
                        if (this.lastCommentSectionHash != null || writer.checkError()) writer.println();
                        for (String commentLine : sectionAnnotation.value()) {
                            writer.println(indent + "# " + commentLine);
                        }
                        this.lastCommentSectionHash = currentSectionHash;
                    }
                }
            }

            // Handle field comments
            Comment fieldComment = field.getAnnotation(Comment.class);
            if (fieldComment != null && fieldComment.value().length > 0) {
                for (String commentLine : fieldComment.value()) {
                    writer.println(indent + "# " + commentLine);
                }
            }

            writer.print(indent + yamlKey + ":");
            Object value;
            try {
                value = field.get(instance);
            } catch (IllegalAccessException e) {
                System.err.println("ERROR: Could not access field " + field.getName() + " during save.");
                continue;
            }

            if (value == null) {
                writer.println(" null");
            } else if (ConfigurablePojo.class.isAssignableFrom(field.getType())) {
                // Handle nested ConfigurablePojo
                writer.println();
                saveFieldsWithComments(value, writer, indentLevel + 1);
            } else {
                // Handle other values
                String valueAsYaml = this.valueDumper.dump(value);

                if (valueAsYaml.endsWith(System.lineSeparator())) {
                    valueAsYaml = valueAsYaml.substring(0, valueAsYaml.length() - System.lineSeparator().length());
                }

                boolean isScalarOrFlowCollection =
                        !(value instanceof List || value instanceof Map)
                                && !valueAsYaml.contains(System.lineSeparator());
                if (value instanceof List && ((List<?>) value).isEmpty()) isScalarOrFlowCollection = true;
                if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) isScalarOrFlowCollection = true;

                if (isScalarOrFlowCollection) {
                    writer.println(" " + valueAsYaml.trim());
                } else {
                    writer.println();
                    valueAsYaml
                            .lines()
                            .forEach(line -> writer.println(indent + "  " + line));
                }
            }

            // Logic for blank line after entry (only at root level)
            if (indentLevel == 0) {
                boolean addBlankLine = false;
                if (fieldIdx < fields.size() - 1) {
                    for (int k = fieldIdx + 1; k < fields.size(); k++) {
                        Field nextField = fields.get(k);
                        if (Modifier.isStatic(nextField.getModifiers())
                                || Modifier.isTransient(nextField.getModifiers())) continue;
                        if (nextField.getAnnotation(Key.class) != null) {
                            addBlankLine = true;
                            break;
                        }
                    }
                }
                if (addBlankLine) {
                    writer.println();
                }
            }
        }
    }

    // DataExtractor implementation for YAML
    private static class YamlDataExtractor implements DataExtractor {
        private final Object yamlData;

        YamlDataExtractor(Object yamlData) {
            this.yamlData = yamlData;
        }

        @Override
        public boolean hasValue(String key) {
            if (!(yamlData instanceof Map)) return false;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            return yamlMap.containsKey(key);
        }

        @Override
        public Object getValue(String key, Class<?> targetType) {
            if (!(yamlData instanceof Map)) return null;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            Object value = yamlMap.get(key);

            if (value == null) {
                return null;
            }

            // Check if the target type is a ConfigurablePojo
            if (ConfigurablePojo.class.isAssignableFrom(targetType)) {
                if (value instanceof Map) {
                    try {
                        // Create an instance of the ConfigurablePojo
                        @SuppressWarnings("unchecked")
                        ConfigurablePojo<?> nestedInstance = (ConfigurablePojo<?>) targetType.getDeclaredConstructor().newInstance();

                        // Recursively apply the nested data
                        YamlDataExtractor nestedExtractor = new YamlDataExtractor(value);
                        applyDataToNestedPojo(nestedInstance, nestedExtractor);

                        return nestedInstance;
                    } catch (Exception e) {
                        System.err.println("ERROR: Could not create and populate instance of " + targetType.getName() +
                                " for key " + key + ": " + e.getMessage());
                        return null;
                    }
                } else {
                    // If the value is not a Map but target is ConfigurablePojo, return null
                    System.err.println("WARNING: Expected Map for ConfigurablePojo field '" + key + "' but got " + value.getClass().getSimpleName());
                    return null;
                }
            }

            // Handle basic type conversions - let the parent's convertNumericIfNeeded handle numeric conversions
            return convertValue(value, targetType);
        }

        /**
         * Apply data to a nested ConfigurablePojo
         */
        private void applyDataToNestedPojo(ConfigurablePojo<?> instance, YamlDataExtractor extractor) throws Exception {
            List<Field> fields = ClassUtils.getAllFieldsInHierarchy(instance.getClass(), ConfigurablePojo.class);

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }

                Key keyAnnotation = field.getAnnotation(Key.class);
                if (keyAnnotation == null) continue;

                String yamlKey = keyAnnotation.value().isEmpty() ? field.getName() : keyAnnotation.value();

                if (!extractor.hasValue(yamlKey)) continue;

                field.setAccessible(true);
                Object yamlValue = extractor.getValue(yamlKey, field.getType());
                field.set(instance, yamlValue);
            }
        }

        /**
         * Convert YAML value to the target type (excluding numeric conversions which are handled by parent)
         */
        private Object convertValue(Object yamlValue, Class<?> targetType) {
            if (yamlValue == null) {
                return null;
            }

            // If the value is already of the correct type, return it
            if (targetType.isAssignableFrom(yamlValue.getClass())) {
                return yamlValue;
            }

            // Handle String conversions
            if (targetType == String.class) {
                return yamlValue.toString();
            }

            // Handle Boolean conversions
            if (targetType == Boolean.class || targetType == boolean.class) {
                if (yamlValue instanceof Boolean) {
                    return yamlValue;
                } else {
                    return Boolean.parseBoolean(yamlValue.toString());
                }
            }

            // For numeric types, let the parent class handle them via convertNumericIfNeeded
            // For other types, try to return the value as-is
            return yamlValue;
        }
    }
}