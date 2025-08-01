package net.astopia.knowledgesystem;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.commandsystem.api.CommandApi;
import net.astopia.guipaperplugin.api.gui.GuiApi;
import net.astopia.knowledgesystem.command.KnowledgeCommand;
import net.astopia.knowledgesystem.listener.PlayerListener;
import net.astopia.knowledgesystem.manager.KnowledgeManager;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.unnamed.common.database.mysql.MySqlDatabase;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KnowledgeSystemPlugin extends JavaPlugin {
    KnowledgeManager knowledgeManager;
    GuiApi guiApi;

    @Override
    public void onEnable() {
        PaperConnectorApi paperConnectorApi = PaperConnectorApi.getInstance();
        MySqlDatabase mySqlDatabase = paperConnectorApi.getMySqlDatabase();

        this.knowledgeManager = new KnowledgeManager(getDataPath(), mySqlDatabase.getDataSource());
        this.guiApi = GuiApi.getInstance();

        getServer().getPluginManager().registerEvents(
                new PlayerListener(knowledgeManager),
                this
        );

        CommandApi commandApi = CommandApi.getInstance();
        commandApi.registerCommand(new KnowledgeCommand(guiApi, knowledgeManager));
    }

    @Override
    public void onDisable() {

    }
}
