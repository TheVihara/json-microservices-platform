package net.unnamed.minecraft.paper.knowledge.gui.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.unnamed.service.gui.module.item.PaperItem;
import org.bukkit.inventory.ItemStack;

@SuperBuilder
@Getter
@Setter
public class KnowledgeItem extends PaperItem {
    String key;
    String wikiUrl;
    int position;

    public KnowledgeItem(@NonNull ItemStack itemStack) {
        super(itemStack);
    }
}
