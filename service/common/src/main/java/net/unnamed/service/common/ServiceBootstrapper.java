package net.unnamed.service.common;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import net.unnamed.service.common.config.ServiceConfig;
import net.unnamed.service.common.terminal.Terminal;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CountDownLatch;

public class ServiceBootstrapper {
    private static ServiceConfig serviceConfig;

    public static void main(String[] args) {
        try {
            InputStream in = ServiceBootstrapper.class.getResourceAsStream("/service.yml");
            Path tempFile = Files.createTempFile("service", ".yml");
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            serviceConfig = ConfigurationLoader.load(tempFile, ServiceConfig::new);

            Class<?> mainClass = Class.forName(serviceConfig.getMainClass());
            Object instance = mainClass.getDeclaredConstructor().newInstance();

            if (!(instance instanceof PlatformService service)) {
                throw new IllegalArgumentException("Main class does not implement PlatformService: " + serviceConfig.getMainClass());
            }

            service.load();

            CountDownLatch shutdownLatch = new CountDownLatch(1);

            Terminal terminal = new Terminal();
            terminal.start(service::onInput);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received. Stopping service...");
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    terminal.stop();
                    shutdownLatch.countDown();
                }
            }));

            shutdownLatch.await();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
