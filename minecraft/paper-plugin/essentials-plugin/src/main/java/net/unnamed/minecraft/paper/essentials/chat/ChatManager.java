package net.unnamed.minecraft.paper.essentials.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.unnamed.common.config.YamlConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ChatManager {
    private ChatConfig config;
    private ChatFormat formatter;
    private final ChatRenderer chatRenderer;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ChatManager(Path dataFolder) {
        this.config = YamlConfig.loadSafe(
                ChatConfig.class,
                dataFolder.resolve("chat.yml"),
                ChatConfig::new
        );
        this.chatRenderer = new CustomChatRenderer(this);
    }

    public void load(Consumer<Listener> registrar) {
        setConfigFormat();
        registrar.accept(new ChatListener(this));
    }

    public Component format(Player player, Component message) {
        if (formatter == null) {
            return message;
        }
        return formatter.format(player, message);
    }

    public void reload() {
        config.reload();
        if (!(formatter instanceof CustomChatFormat)) {
            setConfigFormat();
        }
    }

    private Component configFormatMessage(Player player, Component message) {
        String plainMessage = LegacyComponentSerializer.legacySection().serialize(message);

        String formattedString = PlaceholderAPI.setPlaceholders(player,
                config.getFormat().replace("%message%", plainMessage));

        return parseComponent(formattedString);
    }

    private Component parseComponent(String text) {
        try {
            return miniMessage.deserialize(text);
        } catch (Exception e) {
            return LegacyComponentSerializer.legacySection().deserialize(text);
        }
    }

    public void setConfigFormat() {
        formatter = this::configFormatMessage;
    }

    public ChatRenderer getChatRenderer() {
        return chatRenderer;
    }

    public ChatConfig getConfig() {
        return config;
    }

    public void setConfig(ChatConfig config) {
        this.config = config;
    }

    public void setFormatter(ChatFormat formatter) {
        this.formatter = formatter;
    }

    private static class CustomChatRenderer implements ChatRenderer {
        private final ChatManager chatManager;

        public CustomChatRenderer(ChatManager chatManager) {
            this.chatManager = chatManager;
        }

        @Override
        public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
            Component formatted = chatManager.format(source, message);

            if (formatted == message && chatManager.formatter != null) {
                return formatted;
            }

            return formatted;
        }
    }

    public static class ChatListener implements Listener {
        private final ChatManager chatManager;

        public ChatListener(ChatManager chatManager) {
            this.chatManager = chatManager;
        }

        @EventHandler
        public void onChat(AsyncChatEvent event) {
            event.renderer(chatManager.getChatRenderer());
        }
    }
}