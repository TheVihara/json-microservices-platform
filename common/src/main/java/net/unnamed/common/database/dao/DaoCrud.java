package net.unnamed.common.database.dao;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DaoCrud<T, ID> {
    CompletableFuture<Boolean> init();
    CompletableFuture<T> getById(ID id);
    CompletableFuture<List<T>> getAll();
    CompletableFuture<Boolean> save(T t);
    CompletableFuture<Boolean> deleteById(ID id);
}
