package net.unnamed.service.common;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import net.unnamed.service.common.config.ServiceConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ServiceBootstrapper {
    private static ServiceConfig serviceConfig;

    public static void main(String[] args) {
        Path configFile = Paths.get("service.yml");
        serviceConfig = ConfigurationLoader.load(configFile, ServiceConfig::new);

        try {
            Class<?> mainClass = Class.forName(serviceConfig.getMainClass());
            Object instance = mainClass.getDeclaredConstructor().newInstance();

            if (instance instanceof Service service) {
                service.load();
            } else {
                throw new IllegalArgumentException("Main class does not implement Service interface: " + serviceConfig.getMainClass());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
