package net.unnamed.common.database.dao;

import net.unnamed.common.database.Persisted;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class PersistedDaoCrud<T, ID> implements DaoCrud<T, ID> {

    protected final ConcurrentMap<ID, Persisted<T>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    protected PersistedDaoCrud() {
        scheduler.scheduleAtFixedRate(this::flush, 1, 10, TimeUnit.SECONDS);
    }

    protected abstract ID getId(T entity);
    protected abstract CompletableFuture<T> loadFromDatabase(ID id);

    @Override
    public CompletableFuture<T> getById(ID id) {
        Persisted<T> cached = cache.get(id);
        if (cached != null && !cached.isRemoved()) {
            return CompletableFuture.completedFuture(cached.getInstance());
        }

        return loadFromDatabase(id).thenApply(entity -> {
            if (entity != null) {
                cache.put(id, new Persisted<>(entity, false));
            }
            return entity;
        });
    }

    @Override
    public CompletableFuture<List<T>> getAll() {
        return CompletableFuture.supplyAsync(() ->
                cache.values().stream()
                        .filter(p -> !p.isRemoved())
                        .map(Persisted::getInstance)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public CompletableFuture<T> save(T t) {
        ID id = getId(t);
        Persisted<T> persisted = cache.compute(id, (key, existing) -> {
            if (existing == null) return new Persisted<>(t, true);
            existing.setUpdated(true);
            return existing;
        });

        return CompletableFuture.completedFuture(persisted.getInstance());
    }

    @Override
    public CompletableFuture<Boolean> deleteById(ID id) {
        Persisted<T> persisted = cache.get(id);
        if (persisted != null) {
            persisted.setRemoved(true);
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.completedFuture(false);
    }

    public void shutdown() {
        scheduler.shutdown();
        flush();
    }

    public void flush() {
        List<CompletableFuture<?>> ops = cache.entrySet().stream().map(entry -> {
            ID id = entry.getKey();
            Persisted<T> persisted = entry.getValue();

            if (persisted.isRemoved()) {
                return deleteByIdFromDatabase(id).thenAccept(success -> {
                    if (success) cache.remove(id);
                });
            }

            if (persisted.isAdded() || persisted.isUpdated()) {
                persisted.setAdded(false);
                persisted.setUpdated(false);
                return saveToDatabase(persisted.getInstance()).thenAccept(saved -> {
                    // saved: can update timestamps, etc.
                });
            }

            return CompletableFuture.completedFuture(null);
        }).collect(Collectors.toList());

        CompletableFuture.allOf(ops.toArray(new CompletableFuture[0])).join();
    }

    protected abstract CompletableFuture<T> saveToDatabase(T entity);
    protected abstract CompletableFuture<Boolean> deleteByIdFromDatabase(ID id);
}
