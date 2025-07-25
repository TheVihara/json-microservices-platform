package net.unnamed.minecraft.paper.knowledge.gui;

import net.kyori.adventure.text.Component;
import net.unnamed.minecraft.paper.knowledge.gui.config.KnowledgeConfig;
import net.unnamed.minecraft.paper.knowledge.gui.item.KnowledgeItem;
import net.unnamed.minecraft.paper.knowledge.manager.KnowledgeManager;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.layer.PaginatedInventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;
import net.unnamed.service.gui.module.inventory.PaperChestInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KnowledgeListGui extends PaperChestInventory {
    KnowledgeManager knowledgeManager;
    PaginatedInventoryLayer<KnowledgeItem> knowledgeListLayer;

    public KnowledgeListGui(KnowledgeManager knowledgeManager) {
        super(Component.text("Test"), 6, 9);
        this.knowledgeManager = knowledgeManager;

        List<KnowledgeConfig> knowledgeEntries = knowledgeManager.getKnowledgeEntries();

        Coords first = Coords.of(0, 1);
        Coords second = Coords.of(5, 4);

        int itemsPerPage = (Math.abs(second.getX() - first.getX()) + 1) * (Math.abs(second.getY() - first.getY()) + 1);

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
                                KnowledgeItem knowledgeItem = new KnowledgeItem(new ItemStack(Material.BOOK));
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
                })
                .build();

        knowledgeListLayer.init();
        slotHandler.addLayer("knowledge_list", knowledgeListLayer);
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
