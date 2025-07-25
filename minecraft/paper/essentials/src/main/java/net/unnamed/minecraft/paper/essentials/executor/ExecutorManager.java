package net.unnamed.minecraft.paper.essentials.executor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.essentials.api.executor.Executor;
import net.unnamed.minecraft.paper.essentials.api.executor.ExecutorApi;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SuppressWarnings("unchecked")
public class ExecutorManager implements ExecutorApi {
    ConcurrentHashMap<Object, Set<Executor<?>>> executors = new ConcurrentHashMap<>();

    @Override
    public <E extends Executor<C>, C> void register(Object object, E executor) {
        executors.computeIfAbsent(object, k -> ConcurrentHashMap.newKeySet()).add(executor);
    }

    public <E extends Executor<C>, C> Set<E> get(Object object) {
        return (Set<E>) executors.get(object);
    }

    public <E extends Executor<C>, C> void execute(Object object, C context) {
        Set<E> set = get(object);
        if (set == null) {
            return;
        }
        for (E executor : set) {
            executor.execute(context);
        }
    }
}
