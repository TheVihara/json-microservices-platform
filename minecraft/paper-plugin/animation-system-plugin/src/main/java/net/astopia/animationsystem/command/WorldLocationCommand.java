package net.astopia.animationsystem.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;

public class WorldLocationCommand {

    @Command("worldlocation")
    public void transitionTo(CommandSourceStack stack) {
        Player player = (Player) stack.getSender();
        Location location = player.getLocation();

        String locString = String.format("%.2f %.2f %.2f %.2f %.2f",
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        TextComponent component = Component.text(locString)
                .color(NamedTextColor.GREEN)
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy")))
                .clickEvent(ClickEvent.copyToClipboard(locString));

        player.sendMessage(Component.text("Your current location: ", NamedTextColor.GRAY).append(component));
    }
}
