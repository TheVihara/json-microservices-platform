package net.unnamed.service.common.terminal;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Terminal {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Scanner scanner = new Scanner(System.in);
    private volatile boolean running = false;

    public void start(Consumer<String> inputHandler) {
        if (running) return;
        running = true;

        executor.submit(() -> {
            while (running) {
                if (!scanner.hasNextLine()) {
                    continue;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    stop();
                    break;
                }

                try {
                    inputHandler.accept(input);
                } catch (Exception e) {
                    System.out.println("Error while handling input: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() {
        running = false;
        executor.shutdownNow();
        System.out.println("Terminal stopped.");
    }

    public boolean isRunning() {
        return running;
    }
}
