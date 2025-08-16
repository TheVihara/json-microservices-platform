package net.astopia.animationsystem;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.animation.manager.AnimationManager;
import net.astopia.animationsystem.command.AnimationCommand;
import net.astopia.animationsystem.command.WorldLocationCommand;
import net.astopia.commandsystem.api.CommandApi;
import net.astopia.guipaperplugin.api.gui.GuiApi;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.unnamed.common.database.mysql.MySqlDatabase;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimationSystemPlugin extends JavaPlugin {
    GuiApi guiApi;

    @Override
    public void onEnable() {
        AnimationManager animationManager = new AnimationManager();
        PaperConnectorApi paperConnectorApi = PaperConnectorApi.getInstance();
        MySqlDatabase mySqlDatabase = paperConnectorApi.getMySqlDatabase();

        this.guiApi = GuiApi.getInstance();


        CommandApi commandApi = CommandApi.getInstance();
        commandApi.registerCommand(new AnimationCommand(animationManager, this));
        commandApi.registerCommand(new WorldLocationCommand());
    }

    @Override
    public void onDisable() {

    }
}
