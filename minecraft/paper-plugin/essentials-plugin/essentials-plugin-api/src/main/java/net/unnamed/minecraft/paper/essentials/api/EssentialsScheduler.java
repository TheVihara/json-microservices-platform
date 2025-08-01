package net.unnamed.minecraft.paper.essentials.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.*;

/**
 * Hybrid scheduler combining Bukkit's scheduler with a custom executor
 * for heavy async tasks.
 */
public class EssentialsScheduler {
    private final Plugin plugin;
    private final ScheduledExecutorService heavyExecutor;

    public EssentialsScheduler(Plugin plugin) {
        this.plugin = plugin;
        // Change pool size if you need more parallelism
        this.heavyExecutor = Executors.newScheduledThreadPool(2);
    }

    /**
     * Runs a task on the main server thread immediately.
     */
    public void runSync(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    /**
     * Runs a task asynchronously using Bukkit's thread pool.
     */
    public void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * Runs a task after a delay on the main server thread.
     * @param delayTicks Delay in server ticks (20 ticks = 1 second)
     */
    public void runSyncLater(Runnable task, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }

    /**
     * Runs a task repeatedly on the main server thread.
     * @param initialDelayTicks Initial delay in ticks
     * @param periodTicks Period between runs in ticks
     */
    public void runSyncRepeating(Runnable task, long initialDelayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, initialDelayTicks, periodTicks);
    }

    /**
     * Runs a task after a delay using Bukkit's async thread pool.
     * @param delayTicks Delay in server ticks
     */
    public void runAsyncLater(Runnable task, long delayTicks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks);
    }

    /**
     * Runs a task repeatedly asynchronously using Bukkit's async thread pool.
     * @param initialDelayTicks Initial delay in ticks
     * @param periodTicks Period between runs in ticks
     */
    public void runAsyncRepeating(Runnable task, long initialDelayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, initialDelayTicks, periodTicks);
    }

    /**
     * Runs a heavy async task immediately using your plugin's dedicated executor.
     */
    public Future<?> runHeavyAsync(Runnable task) {
        return heavyExecutor.submit(task);
    }

    /**
     * Schedules a heavy async task to run once after a delay.
     * @param delay Delay
     * @param unit Time unit
     */
    public ScheduledFuture<?> runHeavyScheduled(Runnable task, long delay, TimeUnit unit) {
        return heavyExecutor.schedule(task, delay, unit);
    }

    /**
     * Schedules a heavy async task to run repeatedly.
     * Note: tasks may overlap if previous run didn't finish before next run.
     * @param initialDelay Initial delay before first run
     * @param period Period between runs
     * @param unit Time unit
     */
    public ScheduledFuture<?> runHeavyRepeating(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return heavyExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * Properly shuts down the heavy executor.
     * Call this when your plugin disables.
     */
    public void shutdown() {
        heavyExecutor.shutdown();
        try {
            if (!heavyExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                heavyExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            heavyExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
