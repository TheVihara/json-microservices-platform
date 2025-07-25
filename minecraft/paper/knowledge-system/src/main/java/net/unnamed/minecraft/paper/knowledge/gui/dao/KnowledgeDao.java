package net.unnamed.minecraft.paper.knowledge.gui.dao;

import net.unnamed.common.database.dao.DaoCrud;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class KnowledgeDao implements DaoCrud<String, UUID> {
    @Override
    public CompletableFuture<Boolean> init() {
        return null;
    }

    @Override
    public CompletableFuture<String> getById(UUID uuid) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> getAll() {
        return null;
    }

    @Override
    public CompletableFuture<String> save(String s) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> deleteById(UUID uuid) {
        return null;
    }
}
