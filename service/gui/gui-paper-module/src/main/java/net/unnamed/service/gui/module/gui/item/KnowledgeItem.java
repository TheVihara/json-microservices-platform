package net.unnamed.service.gui.module.gui.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.unnamed.service.gui.module.item.PaperItem;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(level = AccessLevel.PRIVATE)
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
