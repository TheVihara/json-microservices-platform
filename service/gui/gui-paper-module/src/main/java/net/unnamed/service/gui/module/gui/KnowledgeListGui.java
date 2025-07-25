package net.unnamed.service.gui.module.gui;

import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.layer.PaginatedInventoryLayer;
import net.unnamed.service.gui.api.layer.SimpleInventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;
import net.unnamed.service.gui.module.gui.config.KnowledgeConfig;
import net.unnamed.service.gui.module.gui.item.KnowledgeItem;
import net.unnamed.service.gui.module.inventory.PaperChestInventory;
import net.unnamed.service.gui.module.item.PaperItem;
import net.unnamed.service.gui.module.manager.KnowledgeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public KnowledgeListGui(KnowledgeManager knowledgeManager) {
        super(Component.text("Test"), 6, 9);
        this.knowledgeManager = knowledgeManager;

        List<KnowledgeConfig> knowledgeEntries = knowledgeManager.getKnowledgeEntries();

        Bukkit.getLogger().info("Loaded " + knowledgeEntries.size() + " knowledge entries");

        Coords first = Coords.of(0, 1);
        Coords second = Coords.of(5, 4);

        int itemsPerPage = (Math.abs(second.getX() - first.getX()) + 1) * (Math.abs(second.getY() - first.getY()) + 1);
        navigationLayer = SimpleInventoryLayer.builder()
                .weight(20)
                .firstCoords(Coords.of(0, 5))
                .secondCoords(Coords.of(8, 5))
                .build();

        Slot nextPageSlot = Slot.of(
                Coords.of(3, 5),
                navigationLayer,
                this,
                new PaperItem(new ItemStack(Material.ARROW))
        );

        Slot previousPageSlot = Slot.of(
                Coords.of(2, 5),
                navigationLayer,
                this,
                new PaperItem(new ItemStack(Material.ARROW))
        );

        navigationLayer.setSlot(nextPageSlot.getCoords(), nextPageSlot);
        navigationLayer.setSlot(previousPageSlot.getCoords(), previousPageSlot);

        navigationLayer.setVisible(true);

        knowledgeListLayer = PaginatedInventoryLayer.<KnowledgeItem>paginatedBuilder()
                .weight(10)
                .firstCoords(first)
                .secondCoords(second)
                .pageCount((int) Math.ceil((double) knowledgeEntries.size() / itemsPerPage))
                .pageSupplier(page -> {
                    int start = (page - 1) * itemsPerPage;
                    int end = Math.min(start + itemsPerPage, knowledgeEntries.size());

                    return knowledgeEntries.subList(start, end).stream()
                            .map((entry) -> {
                                KnowledgeItem knowledgeItem = new KnowledgeItem(new ItemStack(ITEM_MATERIALS.get(RANDOM.nextInt(ITEM_MATERIALS.size()))));
                                knowledgeItem.setKey(entry.getKey());
                                knowledgeItem.setWikiUrl(entry.getWikiUrl());
                                knowledgeItem.setPosition(entry.getPosition());
                                return knowledgeItem;
                            })
                            .collect(Collectors.toSet());
                })
                .itemSort(Comparator.comparing(KnowledgeItem::getPosition))
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
