package net.unnamed.minecraft.paper.essentials.api.executor;

public interface ExecutorApi {
    <E extends Executor<C>, C> void register(Object object, E executor);
}
