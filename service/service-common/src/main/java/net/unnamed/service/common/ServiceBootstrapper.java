package net.unnamed.service.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.common.config.ServiceConfig;
import net.unnamed.service.common.terminal.Terminal;

import java.util.concurrent.CountDownLatch;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceBootstrapper {
    static ServiceConfig serviceConfig = YamlConfig.loadResourceOnlySafe(ServiceConfig.class, "/service.yml");

    public static void main(String[] args) {
        try {
            Class<?> mainClass = Class.forName(serviceConfig.getMainClass());
            Object instance = mainClass.getDeclaredConstructor().newInstance();

            if (!(instance instanceof PlatformService service)) {
                throw new IllegalArgumentException("Main class does not implement PlatformService: " + serviceConfig.getMainClass());
            }

            service.setName(serviceConfig.getName());
            service.setDescription(serviceConfig.getDescription());

            service.load();

            CountDownLatch shutdownLatch = new CountDownLatch(1);

            Terminal terminal = new Terminal();
            terminal.start(service::onInput);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received. Stopping service...");
                try {
                    service.stop();
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
