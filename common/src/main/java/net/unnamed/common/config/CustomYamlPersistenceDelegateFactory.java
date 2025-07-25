package net.unnamed.common.config;

import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import de.bsommerfeld.jshepherd.core.PersistenceDelegate;
import de.bsommerfeld.jshepherd.core.PersistenceDelegateFactory;
import de.bsommerfeld.jshepherd.core.PersistenceDelegateFactoryRegistry;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * A factory class for creating YAML-specific persistence delegates. This implementation of {@code
 * PersistenceDelegateFactory} supports persistence operations for configuration POJOs in YAML
 * format.
 */
public class CustomYamlPersistenceDelegateFactory implements PersistenceDelegateFactory {

    public CustomYamlPersistenceDelegateFactory() {
        PersistenceDelegateFactoryRegistry.registerFactory(this);
    }

    @Override
    public String[] getSupportedExtensions() {
        return new String[] {"yml", "yaml"};
    }

    @Override
    public <T extends ConfigurablePojo<T>> PersistenceDelegate<T> create(
            Path filePath, boolean useComplexSaveWithComments) {
        return new CustomYamlPersistenceDelegate<>(filePath, useComplexSaveWithComments);
    }
}