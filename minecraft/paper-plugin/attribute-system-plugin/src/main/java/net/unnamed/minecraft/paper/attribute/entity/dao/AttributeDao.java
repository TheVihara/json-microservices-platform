package net.unnamed.minecraft.paper.attribute.entity.dao;

import net.unnamed.common.database.dao.DaoCrud;
import net.unnamed.minecraft.paper.attribute.entity.PlayerAttribute;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AttributeDao implements DaoCrud<Set<PlayerAttribute>, Integer> {
    @Override
    public CompletableFuture<Boolean> init() {
        return null;
    }

    @Override
    public CompletableFuture<Set<PlayerAttribute>> getById(Integer integer) {
        return null;
    }

    @Override
    public CompletableFuture<List<Set<PlayerAttribute>>> getAll() {
        return null;
    }

    @Override
    public CompletableFuture<Set<PlayerAttribute>> save(Set<PlayerAttribute> playerAttributes) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Integer integer) {
        return null;
    }
}
