package net.unnamed.minecraft.paper.attribute.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.attribute.executor.AttributeLoadExecutor;
import net.unnamed.minecraft.paper.essentials.api.executor.ExecutorApi;
import net.unnamed.minecraft.paper.essentials.api.player.executor.PlayerLoadExecutor;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AttributeManager {
    ExecutorApi executorApi;

    public void load() {
        executorApi.register(PlayerLoadExecutor.class, new AttributeLoadExecutor());
    }
}
