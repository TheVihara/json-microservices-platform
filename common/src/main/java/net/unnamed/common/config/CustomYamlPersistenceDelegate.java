package net.unnamed.common.config;

import de.bsommerfeld.jshepherd.annotation.Comment;
import de.bsommerfeld.jshepherd.annotation.CommentSection;
import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.AbstractPersistenceDelegate;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import de.bsommerfeld.jshepherd.utils.ClassUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomYamlPersistenceDelegate<T extends ConfigurablePojo<T>> extends AbstractPersistenceDelegate<T> {
    private Yaml yaml;
    private Yaml valueDumper;

    public CustomYamlPersistenceDelegate(Path filePath, boolean useComplexSaveWithComments) {
        super(filePath, useComplexSaveWithComments);
    }

    private static class CustomRepresenter extends Representer {
        public CustomRepresenter(DumperOptions options) {
            super(options);
            this.multiRepresenters.put(Enum.class, data -> representScalar(Tag.STR, data.toString()));
        }
    }

    private class KeyAnnotationConstructor extends Constructor {
        public KeyAnnotationConstructor(Class<?> pojoClass, LoaderOptions loaderOptions) {
            super(pojoClass, loaderOptions);
            this.yamlClassConstructors.put(NodeId.mapping, new CustomConstructMapping());
        }

        private class CustomConstructMapping extends ConstructMapping {
            @Override
            protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
                List<Field> fields = ClassUtils.getAllFieldsInHierarchy(object.getClass(), ConfigurablePojo.class);
                Map<String, Field> keyToFieldMap = new HashMap<>();
                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) continue;
                    Key keyAnnotation = field.getAnnotation(Key.class);
                    if (keyAnnotation != null) {
                        String key = keyAnnotation.value().isEmpty() ? field.getName() : keyAnnotation.value();
                        keyToFieldMap.put(key, field);
                    }
                }

                List<NodeTuple> nodeTuples = node.getValue();
                for (NodeTuple tuple : nodeTuples) {
                    Node keyNode = tuple.getKeyNode();
                    Node valueNode = tuple.getValueNode();
                    if (keyNode instanceof ScalarNode) {
                        String yamlKey = ((ScalarNode) keyNode).getValue();
                        Field field = keyToFieldMap.get(yamlKey);
                        if (field != null) {
                            try {
                                field.setAccessible(true);
                                valueNode.setType(field.getType());
                                Object value = constructObject(valueNode);
                                if (value != null) {
                                    if (List.class.isAssignableFrom(field.getType()) && isListOfConfigurablePojos(field)) {
                                        value = constructListOfConfigurablePojos(value, field);
                                    } else if (List.class.isAssignableFrom(field.getType()) && isListOfEnums(field)) {
                                        value = constructListOfEnums(value, field);
                                    } else if (ConfigurablePojo.class.isAssignableFrom(field.getType()) && value instanceof Map) {
                                        LoaderOptions nestedOptions = new LoaderOptions();
                                        Constructor nestedConstructor = new KeyAnnotationConstructor(field.getType(), nestedOptions);
                                        Yaml nestedYaml = new Yaml(nestedConstructor);
                                        value = nestedYaml.loadAs(yaml.dump(value), field.getType());
                                    } else {
                                        value = convertValue(value, field.getType());
                                    }
                                    field.set(object, value);
                                }
                            } catch (IllegalAccessException e) {
                                System.err.println("WARNING: Could not set field '" + field.getName() + "' for key '" + yamlKey + "': " + e.getMessage());
                            }
                        } else {
                            // Fallback to default property-based mapping
                            try {
                                Property property = getProperty(object.getClass(), yamlKey);
                                if (property.isWritable()) {
                                    valueNode.setType(property.getType());
                                    Object value = constructObject(valueNode);
                                    if (value != null) {
                                        property.set(object, value);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("WARNING: Could not set property for key '" + yamlKey + "': " + e.getMessage());
                            }
                        }
                    }
                }
                return object;
            }

            private Object constructListOfConfigurablePojos(Object value, Field field) {
                if (!(value instanceof List)) return value;
                List<?> yamlList = (List<?>) value;
                Class<?> elementType = getListElementType(field);
                List<Object> result = new ArrayList<>();
                if (ConfigurablePojo.class.isAssignableFrom(elementType)) {
                    try {
                        LoaderOptions nestedOptions = new LoaderOptions();
                        Constructor nestedConstructor = new KeyAnnotationConstructor(elementType, nestedOptions);
                        Yaml nestedYaml = new Yaml(nestedConstructor);
                        for (Object item : yamlList) {
                            if (item instanceof Map) {
                                Object deserializedItem = nestedYaml.loadAs(yaml.dump(item), elementType);
                                result.add(deserializedItem);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("ERROR: Could not deserialize list of " + elementType.getName() + ": " + e.getMessage());
                    }
                } else {
                    result.addAll(yamlList);
                }
                return result;
            }

            private Object constructListOfEnums(Object value, Field field) {
                if (!(value instanceof List)) return value;
                List<?> yamlList = (List<?>) value;
                Class<?> elementType = getListElementType(field);
                List<Object> result = new ArrayList<>();
                for (Object item : yamlList) {
                    Object converted = convertToEnum(item, elementType);
                    if (converted != null) {
                        result.add(converted);
                    }
                }
                return result;
            }

            private Object convertValue(Object value, Class<?> targetType) {
                if (value == null) return null;
                if (targetType.isAssignableFrom(value.getClass())) return value;
                if (targetType == String.class) return value.toString();
                if (targetType == Boolean.class || targetType == boolean.class) {
                    if (value instanceof Boolean) return value;
                    return Boolean.parseBoolean(value.toString());
                }
                if (targetType.isEnum()) return convertToEnum(value, targetType);
                return convertNumericIfNeeded(value, targetType);
            }

            @SuppressWarnings("unchecked")
            private Object convertToEnum(Object value, Class<?> enumType) {
                if (value == null) return null;
                String stringValue = value.toString();
                try {
                    return Enum.valueOf((Class<Enum>) enumType, stringValue);
                } catch (IllegalArgumentException e) {
                    for (Object enumConstant : enumType.getEnumConstants()) {
                        if (enumConstant.toString().equalsIgnoreCase(stringValue)) return enumConstant;
                    }
                    System.err.println("WARNING: Could not convert '" + stringValue + "' to enum " + enumType.getSimpleName() +
                            ". Valid values are: " + java.util.Arrays.toString(enumType.getEnumConstants()));
                    return null;
                }
            }
        }
    }

    private void initializeYamlIfNeeded(Class<T> pojoClass) {
        if (this.yaml != null) return;

        DumperOptions mainDumperOptions = new DumperOptions();
        mainDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        mainDumperOptions.setPrettyFlow(true);
        mainDumperOptions.setIndent(2);
        mainDumperOptions.setIndicatorIndent(1);
        mainDumperOptions.setSplitLines(false);
        mainDumperOptions.setAllowUnicode(true);
        mainDumperOptions.setExplicitStart(false);
        mainDumperOptions.setExplicitEnd(false);

        Representer representer = new CustomRepresenter(mainDumperOptions);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        this.yaml = new Yaml(new KeyAnnotationConstructor(pojoClass, loaderOptions), representer, mainDumperOptions);

        DumperOptions valueDumperOptions = new DumperOptions();
        valueDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        valueDumperOptions.setIndent(2);
        valueDumperOptions.setIndicatorIndent(1);
        valueDumperOptions.setSplitLines(false);
        valueDumperOptions.setAllowUnicode(true);
        valueDumperOptions.setExplicitStart(false);
        valueDumperOptions.setExplicitEnd(false);
        this.valueDumper = new Yaml(new CustomRepresenter(valueDumperOptions), valueDumperOptions);
    }

    @Override
    protected boolean tryLoadFromFile(T instance) throws Exception {
        initializeYamlIfNeeded((Class<T>) instance.getClass());

        try (Reader reader = Files.newBufferedReader(filePath)) {
            T loadedInstance = yaml.loadAs(reader, (Class<T>) instance.getClass());
            if (loadedInstance != null) {
                applyDataToInstance(instance, new PojoDataExtractor(loadedInstance));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveSimple(T pojoInstance, Path targetPath) throws IOException {
        initializeYamlIfNeeded((Class<T>) pojoInstance.getClass());

        try (Writer writer = Files.newBufferedWriter(targetPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
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
        initializeYamlIfNeeded((Class<T>) pojoInstance.getClass());

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(targetPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            this.lastCommentSectionHash = null;

            Comment classComment = pojoInstance.getClass().getAnnotation(Comment.class);
            if (classComment != null && classComment.value().length > 0) {
                for (String line : classComment.value()) writer.println("# " + line);
                writer.println();
            }

            saveFieldsWithComments(pojoInstance, writer, 0);
        }
    }

    private void saveFieldsWithComments(Object instance, PrintWriter writer, int indentLevel) throws IOException {
        String indent = "  ".repeat(indentLevel);
        List<Field> fields = ClassUtils.getAllFieldsInHierarchy(instance.getClass(), ConfigurablePojo.class);

        for (int fieldIdx = 0; fieldIdx < fields.size(); fieldIdx++) {
            Field field = fields.get(fieldIdx);
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) continue;
            Key keyAnnotation = field.getAnnotation(Key.class);
            if (keyAnnotation == null) continue;

            String yamlKey = keyAnnotation.value().isEmpty() ? field.getName() : keyAnnotation.value();

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

            Comment fieldComment = field.getAnnotation(Comment.class);
            if (fieldComment != null && fieldComment.value().length > 0) {
                for (String commentLine : fieldComment.value()) {
                    writer.println(indent + "# " + commentLine);
                }
            }

            writer.print(indent + yamlKey + ":");
            Object value;
            try {
                field.setAccessible(true);
                value = field.get(instance);
            } catch (IllegalAccessException e) {
                System.err.println("ERROR: Could not access field " + field.getName() + " during save.");
                continue;
            }

            if (value == null) {
                writer.println(" null");
            } else if (isListOfConfigurablePojos(field)) {
                writer.println();
                saveListOfConfigurablePojos((List<?>) value, writer, indentLevel + 1);
            } else if (ConfigurablePojo.class.isAssignableFrom(field.getType())) {
                writer.println();
                saveFieldsWithComments(value, writer, indentLevel + 1);
            } else {
                String valueAsYaml = this.valueDumper.dump(value);
                if (valueAsYaml.endsWith(System.lineSeparator())) {
                    valueAsYaml = valueAsYaml.substring(0, valueAsYaml.length() - System.lineSeparator().length());
                }

                boolean isScalarOrFlowCollection = !(value instanceof List || value instanceof Map) && !valueAsYaml.contains(System.lineSeparator());
                if (value instanceof List && ((List<?>) value).isEmpty()) isScalarOrFlowCollection = true;
                if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) isScalarOrFlowCollection = true;

                if (isScalarOrFlowCollection) {
                    writer.println(" " + valueAsYaml.trim());
                } else {
                    writer.println();
                    valueAsYaml.lines().forEach(line -> writer.println(indent + "  " + line));
                }
            }

            if (indentLevel == 0) {
                boolean addBlankLine = false;
                if (fieldIdx < fields.size() - 1) {
                    for (int k = fieldIdx + 1; k < fields.size(); k++) {
                        Field nextField = fields.get(k);
                        if (Modifier.isStatic(nextField.getModifiers()) || Modifier.isTransient(nextField.getModifiers())) continue;
                        if (nextField.getAnnotation(Key.class) != null) {
                            addBlankLine = true;
                            break;
                        }
                    }
                }
                if (addBlankLine) writer.println();
            }
        }
    }

    private boolean isListOfConfigurablePojos(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) return false;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;
        ParameterizedType paramType = (ParameterizedType) genericType;
        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) return false;
        Type elementType = actualTypeArguments[0];
        if (!(elementType instanceof Class<?>)) return false;
        Class<?> elementClass = (Class<?>) elementType;
        return ConfigurablePojo.class.isAssignableFrom(elementClass);
    }

    private void saveListOfConfigurablePojos(List<?> list, PrintWriter writer, int indentLevel) throws IOException {
        String indent = "  ".repeat(indentLevel);
        if (list == null || list.isEmpty()) {
            writer.println(indent + "[]");
            return;
        }

        for (Object item : list) {
            writer.println(indent + "-");
            if (item instanceof ConfigurablePojo) {
                saveFieldsWithComments(item, writer, indentLevel + 1);
            } else {
                String valueAsYaml = this.valueDumper.dump(item);
                if (valueAsYaml.endsWith(System.lineSeparator())) {
                    valueAsYaml = valueAsYaml.substring(0, valueAsYaml.length() - System.lineSeparator().length());
                }
                writer.println(indent + "  " + valueAsYaml.trim());
            }
        }
    }

    private boolean isListOfEnums(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) return false;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;
        ParameterizedType paramType = (ParameterizedType) genericType;
        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) return false;
        Type elementType = actualTypeArguments[0];
        if (!(elementType instanceof Class<?>)) return false;
        Class<?> elementClass = (Class<?>) elementType;
        return elementClass.isEnum();
    }

    private Class<?> getListElementType(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return Object.class;
        ParameterizedType paramType = (ParameterizedType) genericType;
        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) return Object.class;
        Type elementType = actualTypeArguments[0];
        if (elementType instanceof Class<?>) return (Class<?>) elementType;
        return Object.class;
    }

    private class PojoDataExtractor implements DataExtractor {
        private final Object pojoData;

        PojoDataExtractor(Object pojoData) {
            this.pojoData = pojoData;
        }

        @Override
        public boolean hasValue(String key) {
            try {
                Field field = findFieldByKey(key);
                if (field == null) return false;
                field.setAccessible(true);
                return field.get(pojoData) != null;
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        @Override
        public Object getValue(String key, Class<?> targetType) {
            try {
                Field field = findFieldByKey(key);
                if (field == null) return null;
                field.setAccessible(true);
                return field.get(pojoData);
            } catch (IllegalAccessException e) {
                System.err.println("WARNING: Could not access field for key '" + key + "': " + e.getMessage());
                return null;
            }
        }

        private Field findFieldByKey(String key) {
            List<Field> fields = ClassUtils.getAllFieldsInHierarchy(pojoData.getClass(), ConfigurablePojo.class);
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) continue;
                Key keyAnnotation = field.getAnnotation(Key.class);
                if (keyAnnotation != null) {
                    String fieldKey = keyAnnotation.value().isEmpty() ? field.getName() : keyAnnotation.value();
                    if (fieldKey.equals(key)) return field;
                }
            }
            return null;
        }
    }

    private class YamlDataExtractor implements DataExtractor {
        private final Object yamlData;
        private final Map<String, Field> keyToFieldMap;

        YamlDataExtractor(Object yamlData, Map<String, Field> keyToFieldMap) {
            this.yamlData = yamlData;
            this.keyToFieldMap = keyToFieldMap;
        }

        @Override
        public boolean hasValue(String key) {
            if (!(yamlData instanceof Map)) return false;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            return yamlMap.containsKey(key);
        }

        @Override
        public Object getValue(String key, Class<?> targetType) {
            if (keyToFieldMap.containsKey(key)) {
                Field field = keyToFieldMap.get(key);
                if (List.class.isAssignableFrom(field.getType())) {
                    if (isListOfConfigurablePojos(field)) {
                        return getListOfConfigurablePojos(key, field);
                    } else if (isListOfEnums(field)) {
                        return getListOfEnums(key, field);
                    } else {
                        return getSimpleList(key, field);
                    }
                } else {
                    return getSimpleValue(key, targetType);
                }
            } else {
                return getSimpleValue(key, targetType);
            }
        }

        private Object getSimpleValue(String key, Class<?> targetType) {
            if (!(yamlData instanceof Map)) return null;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            Object value = yamlMap.get(key);

            if (value == null) return null;

            if (ConfigurablePojo.class.isAssignableFrom(targetType)) {
                if (value instanceof Map) {
                    try {
                        LoaderOptions loaderOptions = new LoaderOptions();
                        Constructor constructor = new KeyAnnotationConstructor(targetType, loaderOptions);
                        Yaml nestedYaml = new Yaml(constructor);
                        return nestedYaml.loadAs(yaml.dump(value), targetType);
                    } catch (Exception e) {
                        System.err.println("ERROR: Could not create and populate instance of " + targetType.getName() +
                                " for key " + key + ": " + e.getMessage());
                        return null;
                    }
                } else {
                    System.err.println("WARNING: Expected Map for ConfigurablePojo field '" + key + "' but got " + value.getClass().getSimpleName());
                    return null;
                }
            }

            return convertValue(value, targetType);
        }

        private Object getListOfConfigurablePojos(String key, Field field) {
            if (!(yamlData instanceof Map)) return null;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            Object value = yamlMap.get(key);
            if (value == null) return null;
            if (!(value instanceof List)) {
                System.err.println("WARNING: Expected List for field '" + key + "' but got " + value.getClass().getSimpleName());
                return null;
            }
            List<?> yamlList = (List<?>) value;
            Class<?> elementType = getListElementType(field);
            List<Object> result = new ArrayList<>();
            if (ConfigurablePojo.class.isAssignableFrom(elementType)) {
                try {
                    LoaderOptions loaderOptions = new LoaderOptions();
                    Constructor constructor = new KeyAnnotationConstructor(elementType, loaderOptions);
                    Yaml nestedYaml = new Yaml(constructor);
                    for (Object item : yamlList) {
                        if (item instanceof Map) {
                            Object deserializedItem = nestedYaml.loadAs(yaml.dump(item), elementType);
                            result.add(deserializedItem);
                        } else {
                            System.err.println("WARNING: Expected Map for ConfigurablePojo list item in key '" + key + "' but got " + item.getClass().getSimpleName());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("ERROR: Could not create and populate instance of " + elementType.getName() +
                            " for list item in key " + key + ": " + e.getMessage());
                }
            } else {
                result.addAll(yamlList);
            }
            return result;
        }

        private Object getListOfEnums(String key, Field field) {
            if (!(yamlData instanceof Map)) return null;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            Object value = yamlMap.get(key);
            if (value == null) return null;
            if (!(value instanceof List)) {
                System.err.println("WARNING: Expected List for field '" + key + "' but got " + value.getClass().getSimpleName());
                return null;
            }
            List<?> yamlList = (List<?>) value;
            Class<?> elementType = getListElementType(field);
            List<Object> result = new ArrayList<>();
            for (Object item : yamlList) {
                Object converted = convertToEnum(item, elementType);
                if (converted != null) {
                    result.add(converted);
                }
            }
            return result;
        }

        private Object getSimpleList(String key, Field field) {
            if (!(yamlData instanceof Map)) return null;
            Map<?, ?> yamlMap = (Map<?, ?>) yamlData;
            return yamlMap.get(key);
        }

        private Object convertValue(Object yamlValue, Class<?> targetType) {
            if (yamlValue == null) return null;
            if (targetType.isAssignableFrom(yamlValue.getClass())) return yamlValue;
            if (targetType == String.class) return yamlValue.toString();
            if (targetType == Boolean.class || targetType == boolean.class) {
                if (yamlValue instanceof Boolean) return yamlValue;
                return Boolean.parseBoolean(yamlValue.toString());
            }
            if (targetType.isEnum()) return convertToEnum(yamlValue, targetType);
            return convertNumericIfNeeded(yamlValue, targetType);
        }

        @SuppressWarnings("unchecked")
        private Object convertToEnum(Object value, Class<?> enumType) {
            if (value == null) return null;
            String stringValue = value.toString();
            try {
                return Enum.valueOf((Class<Enum>) enumType, stringValue);
            } catch (IllegalArgumentException e) {
                for (Object enumConstant : enumType.getEnumConstants()) {
                    if (enumConstant.toString().equalsIgnoreCase(stringValue)) return enumConstant;
                }
                System.err.println("WARNING: Could not convert '" + stringValue + "' to enum " + enumType.getSimpleName() +
                        ". Valid values are: " + java.util.Arrays.toString(enumType.getEnumConstants()));
                return null;
            }
        }
    }
}