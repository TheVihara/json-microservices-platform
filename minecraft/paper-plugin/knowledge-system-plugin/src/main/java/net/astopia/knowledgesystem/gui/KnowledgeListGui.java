package net.astopia.knowledgesystem.gui;

import net.kyori.adventure.text.Component;
import net.astopia.knowledgesystem.manager.KnowledgeManager;
import net.astopia.itemsystem.api.CustomItem;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.layer.pagination.PaginatedInventoryLayer;
import net.unnamed.service.gui.api.layer.SimpleInventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;
import net.astopia.knowledgesystem.config.KnowledgeConfig;
import net.astopia.knowledgesystem.gui.item.KnowledgeItem;
import net.astopia.guipaperplugin.api.inventory.PaperChestInventory;
import net.astopia.guipaperplugin.api.item.PaperItem;
import net.unnamed.service.player.api.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KnowledgeListGui extends PaperChestInventory {
    KnowledgeManager knowledgeManager;
    PaginatedInventoryLayer<KnowledgeItem> knowledgeListLayer;
    SimpleInventoryLayer navigationLayer;

    private static final List<Material> ITEM_MATERIALS = List.of(
            Material.BOOK, Material.PAPER, Material.MAP, Material.WRITABLE_BOOK, Material.KNOWLEDGE_BOOK
    );

    private static final Random RANDOM = new Random();

    private static final ItemStack rightArrow = CustomItem.builder()
            .key("right_arrow")
            .material(Material.ECHO_SHARD)
            .displayName(MiniMessage.miniMessage().deserialize("<yellow>Next Page ->").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    private static final ItemStack leftArrow = CustomItem.builder()
            .key("left_arrow")
            .material(Material.ECHO_SHARD)
            .displayName(MiniMessage.miniMessage().deserialize("<yellow><- Previous Page").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    private static final ItemStack unknown = CustomItem.builder()
            .key("unknown")
            .material(Material.ECHO_SHARD)
            .displayName(MiniMessage.miniMessage().deserialize("<red>Undiscovered").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    private static final ItemStack unlockedUnknown = CustomItem.builder()
            .key("unlocked_unknown")
            .material(Material.ECHO_SHARD)
            .displayName(MiniMessage.miniMessage().deserialize("<yellow>Discover now!").decoration(TextDecoration.ITALIC, false))
            .build().toItemStack();

    public KnowledgeListGui(Player player, KnowledgeManager knowledgeManager) {
        super("\uE002", "header", "knowledge_body", "footer", Component.text("Knowledge Book"), 6, 9);
        this.knowledgeManager = knowledgeManager;

        List<KnowledgeConfig> knowledgeEntries = knowledgeManager.getKnowledgeEntries();
        Bukkit.getLogger().info("Loaded " + knowledgeEntries.size() + " knowledge entries");

        Coords first = Coords.of(2, 1);
        Coords second = Coords.of(6, 3);

        int itemsPerPage = (Math.abs(second.getX() - first.getX()) + 1) * (Math.abs(second.getY() - first.getY()) + 1);

        navigationLayer = SimpleInventoryLayer.builder()
                .weight(20)
                .firstCoords(Coords.of(0, 4))
                .secondCoords(Coords.of(8, 5))
                .build();

        Slot nextPageSlot = Slot.of(
                Coords.of(6, 4),
                navigationLayer,
                this,
                new PaperItem(rightArrow)
        );

        Slot previousPageSlot = Slot.of(
                Coords.of(2, 4),
                navigationLayer,
                this,
                new PaperItem(leftArrow)
        );

        navigationLayer.setSlot(nextPageSlot.getCoords(), nextPageSlot);
        navigationLayer.setSlot(previousPageSlot.getCoords(), previousPageSlot);
        navigationLayer.setVisible(true);

        knowledgeListLayer = PaginatedInventoryLayer.<KnowledgeItem>paginatedBuilder()
                .weight(10)
                .firstCoords(first)
                .secondCoords(second)
                .pageCount((int) Math.ceil((double) knowledgeEntries.size() / itemsPerPage))
                .pageSupplier(pageData -> {
                    PlayerBase<?> playerBase = pageData.viewer().getPlayer();
/*                    int start = (pageData.page() - 1) * itemsPerPage;
                    int end = Math.min(start + itemsPerPage, knowledgeEntries.size());*/

                    return knowledgeEntries.stream()
                            .map((entry) -> {
                                KnowledgeItem knowledgeItem;
                                if (knowledgeManager.hasUnlocked(playerBase.getUuid(), entry.getKey())) {
                                    knowledgeItem = new KnowledgeItem(/*new ItemStack(entry.getMaterial())*/unlockedUnknown);
                                } else {
                                    knowledgeItem = new KnowledgeItem(unknown);
                                }
                                knowledgeItem.setKey(entry.getKey());
                                knowledgeItem.setWikiUrl(entry.getWikiUrl());
                                knowledgeItem.setPosition(entry.getPosition());
                                return knowledgeItem;
                            })
                            .collect(Collectors.toSet());
                })
                .itemSort(Comparator.comparing((KnowledgeItem item) -> !knowledgeManager.hasUnlocked(player.getUniqueId(), item.getKey())).thenComparing(KnowledgeItem::getPosition))
                .onClick(click -> {
                    Slot slot = click.getClickedSlot();
                    KnowledgeItem item = (KnowledgeItem) slot.getItem();

                    click.getViewer().getPlayer().sendMessage(Component.text("Clicked on " + item.getKey()));
                    click.setCancelled(true);
                })
                .nextPageSlot(nextPageSlot)
                .previousPageSlot(previousPageSlot)
                .build();

        knowledgeListLayer.init();
        knowledgeListLayer.setVisible(true);
        slotHandler.addLayer("knowledge_list", knowledgeListLayer);
        slotHandler.addLayer("navigation", navigationLayer);
    }

    @Override
    public void onOpen(InventoryViewer viewer) {
        viewer.getPlayer().sendMessage(Component.text("Opened the knowledge list gui!"));
        draw(viewer);
    }

    @Override
    public void onClose(InventoryViewer viewer) {
        viewer.getPlayer().sendMessage(Component.text("Closed the knowledge list gui!"));
    }

    @Override
    public void onDraw(InventoryViewer viewer) {
        // You may want to redraw or refresh layers if needed
    }
}