package net.unnamed.minecraft.paper.essentials.player;

import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.Component;
import net.unnamed.common.database.dao.DaoCrud;
import net.unnamed.service.player.api.PlayerBase;
import org.bukkit.entity.Player;

import java.net.SocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EssentialsPlayer extends OfflineEssentialsPlayer implements PlayerBase {
    private final Player player;

    public EssentialsPlayer(final Player player) {
        super(-1, player.getUniqueId(), player.getName());
        this.player = player;
    }

    public EssentialsPlayer(Integer id, UUID uuid, String name) {
        super(id, uuid, name);
        this.player = null;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return player != null ? player.getAddress() : null;
    }

    @Override
    public void sendMessage(String message) {
        if (player != null) player.sendMessage(Component.text(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return player != null && player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Component message) {
        if (player != null) player.sendMessage(message);
    }

    @Override
    public boolean isOnline() {
        return player != null && player.isOnline();
    }

    public void update(OfflineEssentialsPlayer player) {
        this.name = player.getName();
        this.uuid = player.getUuid();
    }

    public static class Dao implements DaoCrud<EssentialsPlayer, Integer> {
        private final HikariDataSource dataSource;

        public Dao(HikariDataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public CompletableFuture<Boolean> init() {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {

                    statement.execute("""
                            CREATE TABLE IF NOT EXISTS essentials_player (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                uuid CHAR(36) NOT NULL,
                                name VARCHAR(128) NOT NULL
                            );
                            """);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }

        @Override
        public CompletableFuture<EssentialsPlayer> getById(Integer id) {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT uuid, name FROM essentials_player WHERE id = ?")) {
                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            UUID uuid = UUID.fromString(rs.getString("uuid"));
                            String name = rs.getString("name");
                            return new EssentialsPlayer(id, uuid, name);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

        public CompletableFuture<EssentialsPlayer> getByUUID(UUID uuid) {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT id, name FROM essentials_player WHERE uuid = ?")) {
                    ps.setString(1, uuid.toString());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Integer id = rs.getInt("id");
                            String name = rs.getString("name");
                            return new EssentialsPlayer(id, uuid, name);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

        @Override
        public CompletableFuture<List<EssentialsPlayer>> getAll() {
            return CompletableFuture.supplyAsync(() -> {
                List<EssentialsPlayer> list = new ArrayList<>();
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT id, uuid, name FROM essentials_player");
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Integer id = rs.getInt("id");
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        String name = rs.getString("name");
                        list.add(new EssentialsPlayer(id, uuid, name));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return list;
            });
        }

        @Override
        public CompletableFuture<Boolean> save(EssentialsPlayer player) {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("""
                             INSERT INTO essentials_player (uuid, name)
                             VALUES (?, ?)
                             ON CONFLICT(uuid) DO UPDATE SET name=excluded.name;
                             """)) {
                    ps.setString(1, player.getUuid().toString());
                    ps.setString(2, player.getName());
                    ps.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }

        @Override
        public CompletableFuture<Boolean> deleteById(Integer id) {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM essentials_player WHERE id = ?")) {
                    ps.setInt(1, id);
                    int affected = ps.executeUpdate();
                    return affected > 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }
    }
}
