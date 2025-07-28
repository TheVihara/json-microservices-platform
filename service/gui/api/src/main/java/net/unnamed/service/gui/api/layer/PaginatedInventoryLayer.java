package net.unnamed.service.gui.api.layer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder(builderMethodName = "paginatedBuilder")
public class PaginatedInventoryLayer<T extends Item> extends SimpleInventoryLayer {
    @Builder.Default
    int currentPage = 1;
    Function<Integer, Set<T>> pageSupplier;
    Integer itemCount;
    Integer pageCount;
    Comparator<T> itemSort;
    Consumer<Click> onClick;
    Slot nextPageSlot;
    Slot previousPageSlot;

    public void init() {
        nextPageSlot.setClickConsumer(click -> {
            InventoryLayer layer = nextPageSlot.getLayer();
            if (hasNextPage()) {
                currentPage++;
                nextPageSlot.setVisible(hasNextPage());
                previousPageSlot.setVisible(hasPreviousPage());
                layer.draw(click.getViewer(), click.getInventory());
                draw(click.getViewer(), click.getInventory());
                click.getViewer().getPlayer().sendMessage("Next Current page: " + currentPage + "/" + pageCount + " (" + itemCount + " items)");
            }
            click.setCancelled(true);
        });

        previousPageSlot.setClickConsumer(click -> {
            InventoryLayer layer = nextPageSlot.getLayer();
            if (hasPreviousPage()) {
                currentPage--;
                nextPageSlot.setVisible(hasNextPage());
                previousPageSlot.setVisible(hasPreviousPage());
                layer.draw(click.getViewer(), click.getInventory());
                draw(click.getViewer(), click.getInventory());
                click.getViewer().getPlayer().sendMessage("Prev Current page: " + currentPage + "/" + pageCount + " (" + itemCount + " items)");
            }
            click.setCancelled(true);
        });

        nextPageSlot.setVisible(hasNextPage());
        previousPageSlot.setVisible(hasPreviousPage());
    }

    @Override
    public void onDraw(InventoryViewer viewer, ServiceInventory inventory) {
        refreshPaginatedContent(inventory);
    }

    public void refreshPaginatedContent(ServiceInventory inventory) {
        List<T> items = getComparedItems();

        int index = 0;

        for (int y = firstCoords.getY(); y <= secondCoords.getY(); y++) {
            for (int x = firstCoords.getX(); x <= secondCoords.getX(); x++) {
                if (index >= items.size()) return;

                Coords coords = new Coords(x, y);
                T item = items.get(index++);

                //System.out.println("Setting slot " + coords.getX() + " " + coords.getY() + "  with item " + item);

                setSlot(coords, Slot.of(coords, this, inventory, item, onClick));
            }
        }
    }

    public List<T> getComparedItems() {
        return pageSupplier.apply(currentPage).stream()
                .sorted(itemSort)
                .toList();
    }

    public boolean hasNextPage() {
        return currentPage < pageCount;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
}
