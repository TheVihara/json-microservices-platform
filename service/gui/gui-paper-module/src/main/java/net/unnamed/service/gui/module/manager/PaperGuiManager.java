package net.unnamed.service.gui.module.manager;

import com.alibaba.fastjson2.JSON;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.impl.BasicGui;
import net.unnamed.service.gui.api.impl.ContainerGui;
import net.unnamed.service.gui.api.serializer.SerializerType;
import net.unnamed.service.gui.module.factory.PaperInventoryFactory;
import net.unnamed.service.gui.module.inventory.CustomInventory;
import net.unnamed.service.pack.api.BitMapFont;
import net.unnamed.service.pack.api.NegativeFontRegistry;
import net.unnamed.service.pack.api.dao.BitMapFontDao;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

import static net.unnamed.service.gui.module.GuiModule.MM;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SuppressWarnings("unchecked")
public class PaperGuiManager {
    NegativeFontRegistry fontRegistry = new NegativeFontRegistry();
    BitMapFontDao bitMapFontDao;

    public PaperGuiManager(HikariDataSource dataSource) {
        this.bitMapFontDao = new BitMapFontDao(dataSource);
    }

    public void openGui(UUID uuid, String guiData) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        Gui gui = SerializerType.deserialize(JSON.parseObject(guiData));
        openGui(player, gui);
    }

    public void openGui(Player player, Gui gui) {
        if (gui instanceof ContainerGui) {

        } else if (gui instanceof BasicGui basicGui) {
            CustomInventory customInventory = (CustomInventory) InventoryFactory.<PaperInventoryFactory>getInstance().createInventory(
                    basicGui.getInventory().getType(),
                    basicGui.getInventory().getTitle(),
                    basicGui.getInventory().getRows(),
                    basicGui.getInventory().getColumns()
            );

            player.openInventory(customInventory.getBukkitInventory());
        } else if (gui instanceof CustomInventory customInventory) {
            player.openInventory(customInventory.getBukkitInventory());
        }
    }

    public void setTitle(InventoryOpenEvent event, CustomInventory customInventory) {
        if (customInventory.getHeader() != null) {
            String headerKey = customInventory.getHeader();
            String bodyKey = customInventory.getBody();
            String footerKey = customInventory.getFooter();
            String emoji = customInventory.getEmoji();
            Component text = customInventory.getTitle();
            BitMapFont bitMapHeader = bitMapFontDao.getById(headerKey).join();
            BitMapFont bitMapBody = bitMapFontDao.getById(bodyKey).join();
            BitMapFont bitMapFooter = bitMapFontDao.getById(footerKey).join();
            int headerPadding = bitMapHeader.getWidth() + (bitMapHeader.getWidth() - bitMapBody.getWidth()) / 2;
            int footerPadding = bitMapFooter.getWidth() + (bitMapFooter.getWidth() - bitMapBody.getWidth()) / 2;

            Bukkit.getLogger().info("Header padding: " + headerPadding);
            Bukkit.getLogger().info("Footer padding: " + footerPadding);

            Bukkit.getLogger().info("Emoji padding: " + -(headerPadding-6));
            Bukkit.getLogger().info("Title padding: " + -(headerPadding-30));

            Component title = MM.deserialize(fontRegistry.createComplexPadding(-7, true))
                    .append(Component.text(bitMapBody.getFont()).font(Key.key("astopia", "gui")))
                    .append(MM.deserialize(fontRegistry.createComplexPadding(-headerPadding, true)))
                    .append(Component.text(bitMapHeader.getFont()).font(Key.key("astopia", "gui")))
                    .append(MM.deserialize(fontRegistry.createComplexPadding(-footerPadding, true)))
                    .append(Component.text(bitMapFooter.getFont()).font(Key.key("astopia", "gui")))
                    .append(MM.deserialize(fontRegistry.createComplexPadding(-(headerPadding-4), true)))
                    .append(Component.text(emoji).font(Key.key("astopia", "emoji")))
                    .append(MM.deserialize(fontRegistry.createComplexPadding(7, true)))
                    .append(text.font(Key.key("astopia", "hud_offset_10.0")))
                    .color(NamedTextColor.WHITE);

            event.titleOverride(title);
        } else {
            event.titleOverride(customInventory.getTitle());
        }
    }
}
