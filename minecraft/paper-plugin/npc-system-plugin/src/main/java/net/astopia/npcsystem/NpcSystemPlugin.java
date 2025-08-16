package net.astopia.npcsystem;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.commandsystem.api.CommandApi;
import net.astopia.guipaperplugin.api.gui.GuiApi;
import net.astopia.npcsystem.command.CustomNPCCommand;
import net.astopia.npcsystem.npc.manager.NPCManager;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.unnamed.common.database.mysql.MySqlDatabase;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NpcSystemPlugin extends JavaPlugin {
    GuiApi guiApi;

    @Override
    public void onEnable() {
        NPCManager npcManager = new NPCManager(this);
        PaperConnectorApi paperConnectorApi = PaperConnectorApi.getInstance();
        MySqlDatabase mySqlDatabase = paperConnectorApi.getMySqlDatabase();

        this.guiApi = GuiApi.getInstance();


        CommandApi commandApi = CommandApi.getInstance();
        commandApi.registerCommand(new CustomNPCCommand(npcManager));
    }

    @Override
    public void onDisable() {

    }
}
