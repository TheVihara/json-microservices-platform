package net.astopia.knowledgesystem.command;


import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.guipaperplugin.api.gui.GuiApi;
import net.astopia.knowledgesystem.gui.KnowledgeListGui;
import net.astopia.knowledgesystem.manager.KnowledgeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KnowledgeCommand {
    GuiApi guiApi;
    KnowledgeManager knowledgeManager;

    @Command("knowledge")
    public void knowledge(CommandSourceStack stack) {
        CommandSender commandSender = stack.getSender();
        Player player = (Player) commandSender;

        guiApi.openGui(player, new KnowledgeListGui(player, knowledgeManager));
    }
}
