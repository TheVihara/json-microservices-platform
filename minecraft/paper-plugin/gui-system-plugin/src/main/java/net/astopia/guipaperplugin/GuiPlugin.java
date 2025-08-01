package net.astopia.guipaperplugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.astopia.guipaperplugin.factory.PaperGuiFactory;
import net.astopia.guipaperplugin.factory.PaperInventoryFactory;
import net.astopia.guipaperplugin.listener.PaperGuiListener;
import net.astopia.guipaperplugin.manager.PaperGuiManager;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.astopia.guipaperplugin.api.GuiPluginApi;
import net.astopia.guipaperplugin.api.gui.GuiApi;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class GuiPlugin extends JavaPlugin implements GuiPluginApi {
    public static MiniMessage MM = MiniMessage.miniMessage();

    final PaperGuiFactory guiFactory = new PaperGuiFactory();
    final PaperInventoryFactory inventoryFactory = new PaperInventoryFactory();

    PaperGuiManager guiManager;

    @Override
    public void onEnable() {
        PaperConnectorApi paperConnectorApi = PaperConnectorApi.getInstance();
        MySqlDatabase mySqlDatabase = paperConnectorApi.getMySqlDatabase();

        this.guiManager = new PaperGuiManager(mySqlDatabase.getDataSource());

        getServer().getServicesManager().register(GuiApi.class, guiManager, this, ServicePriority.Normal);

        getServer().getPluginManager().registerEvents(
                new PaperGuiListener(guiManager),
                this
        );
    }
}
