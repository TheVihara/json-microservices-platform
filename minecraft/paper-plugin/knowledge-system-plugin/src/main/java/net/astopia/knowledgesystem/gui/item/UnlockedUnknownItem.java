package net.astopia.knowledgesystem.gui.item;

import net.astopia.guipaperplugin.api.item.AnimatedPaperItem;
import net.astopia.guipaperplugin.api.item.PaperItem;
import net.astopia.itemsystem.api.CustomItem;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class UnlockedUnknownItem extends AnimatedPaperItem {
    private static final ItemStack unlockedUnknown = CustomItem.builder()
            .key("unlocked_unknown")
            .displayName(MiniMessage.miniMessage().deserialize("<gold>Click to unlock!").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    private static final ItemStack unlockedUnknownBlink = CustomItem.builder()
            .key("unlocked_unknown")
            .displayName(MiniMessage.miniMessage().deserialize("<gold>Click to unlock!").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    public UnlockedUnknownItem() {
        super(new LinkedList<>(List.of(
                new PaperItem(unlockedUnknown),
                new PaperItem(unlockedUnknownBlink)
        )), 2);
    }
}
