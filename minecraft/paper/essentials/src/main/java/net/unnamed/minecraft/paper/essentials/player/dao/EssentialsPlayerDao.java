package net.unnamed.minecraft.paper.essentials.player.dao;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.dao.DaoCrud;
import net.unnamed.minecraft.paper.essentials.api.player.EssentialsPlayer;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EssentialsPlayerDao implements DaoCrud<EssentialsPlayer, Integer> {
    HikariDataSource dataSource;

    public EssentialsPlayerDao(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CompletableFuture<Boolean> init() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "CREATE TABLE IF NOT EXISTS essentials_players (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY," +
                                 "uuid VARCHAR(36) NOT NULL UNIQUE," +
                                 "name VARCHAR(16) NOT NULL," +
                                 "ip VARCHAR(45) NULL," +
                                 "last_login TIMESTAMP NULL," +
                                 "balance DOUBLE NOT NULL DEFAULT 0.0)")) {
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to initialize essentials_players table: " + e.getMessage());
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<EssentialsPlayer> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT uuid, name, ip, last_login, balance FROM essentials_players WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String ip = rs.getString("ip");
                    return EssentialsPlayer.builder()
                            .id(id)
                            .uuid(UUID.fromString(rs.getString("uuid")))
                            .name(rs.getString("name"))
                            .socketAddress(ip != null ? InetSocketAddress.createUnresolved(ip, 25565) : null)
                            .lastLogin(rs.getTimestamp("last_login"))
                            .balance(rs.getDouble("balance"))
                            .build();
                }
                return null;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to load player by id " + id + ": " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<EssentialsPlayer> getByName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT id, uuid, ip, last_login, balance FROM essentials_players WHERE name = ?")) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String ip = rs.getString("ip");
                    return EssentialsPlayer.builder()
                            .id(rs.getInt("id"))
                            .uuid(UUID.fromString(rs.getString("uuid")))
                            .name(name)
                            .socketAddress(ip != null ? InetSocketAddress.createUnresolved(ip, 25565) : null)
                            .lastLogin(rs.getTimestamp("last_login"))
                            .balance(rs.getDouble("balance"))
                            .build();
                }
                return null;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to load player by name " + name + ": " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<EssentialsPlayer> getByUUID(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT id, name, ip, last_login, balance FROM essentials_players WHERE uuid = ?")) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return EssentialsPlayer.builder()
                            .id(rs.getInt("id"))
                            .uuid(uuid)
                            .name(rs.getString("name"))
                            .socketAddress(InetSocketAddress.createUnresolved(rs.getString("ip"), 25565))
                            .lastLogin(rs.getTimestamp("last_login"))
                            .balance(rs.getDouble("balance"))
                            .build();
                }
                return null;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to load player by UUID " + uuid + ": " + e.getMessage());
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<List<EssentialsPlayer>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<EssentialsPlayer> players = new ArrayList<>();
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT id, uuid, name, ip, last_login, balance FROM essentials_players");
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String ip = rs.getString("ip");
                    players.add(EssentialsPlayer.builder()
                            .id(rs.getInt("id"))
                            .uuid(UUID.fromString(rs.getString("uuid")))
                            .name(rs.getString("name"))
                            .socketAddress(ip != null ? InetSocketAddress.createUnresolved(ip, 25565) : null)
                            .lastLogin(rs.getTimestamp("last_login"))
                            .balance(rs.getDouble("balance"))
                            .build());
                }
                return players;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to load all players: " + e.getMessage());
                return Collections.emptyList();
            }
        });
    }

    @Override
    public CompletableFuture<EssentialsPlayer> save(EssentialsPlayer essentialsPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection()) {
                Integer id = essentialsPlayer.getId();
                String ip = essentialsPlayer.getSocketAddress() != null ? essentialsPlayer.getSocketAddress().toString() : null;

                if (id == -1) {
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO essentials_players (uuid, name, ip, last_login, balance) VALUES (?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, essentialsPlayer.getUuid().toString());
                        stmt.setString(2, essentialsPlayer.getName());
                        stmt.setString(3, ip);
                        stmt.setTimestamp(4, essentialsPlayer.getLastLogin());
                        stmt.setDouble(5, essentialsPlayer.getBalance());
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            id = rs.getInt(1);
                            essentialsPlayer.setId(id);
                            return essentialsPlayer;
                        }
                    }
                } else {
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE essentials_players SET name = ?, ip = ?, last_login = ?, balance = ? WHERE id = ?")) {
                        stmt.setString(1, essentialsPlayer.getName());
                        stmt.setString(2, ip);
                        stmt.setTimestamp(3, essentialsPlayer.getLastLogin());
                        stmt.setDouble(4, essentialsPlayer.getBalance());
                        stmt.setInt(5, id);
                        stmt.executeUpdate();
                        return essentialsPlayer;
                    }
                }
                return essentialsPlayer;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to save player " + essentialsPlayer.getUuid() + ": " + e.getMessage());
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM essentials_players WHERE id = ?")) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to delete player by id " + id + ": " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> deleteByUUID(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM essentials_players WHERE uuid = ?")) {
                stmt.setString(1, uuid.toString());
                int rows = stmt.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to delete player by UUID " + uuid + ": " + e.getMessage());
                return false;
            }
        });
    }
}