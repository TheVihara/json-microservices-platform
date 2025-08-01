package net.astopia.knowledgesystem.dao;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.dao.DaoCrud;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KnowledgeDao implements DaoCrud<String, UUID> {
    HikariDataSource dataSource;
    ExecutorService executor = Executors.newFixedThreadPool(4);

    public KnowledgeDao(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        init(); // async init
    }

    @Override
    public CompletableFuture<Boolean> init() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS player_knowledge (
                         player_uuid UUID NOT NULL,
                         knowledge_key VARCHAR(255) NOT NULL,
                         time_unlocked TIMESTAMP NOT NULL,
                         PRIMARY KEY (player_uuid, knowledge_key)
                     )
                 """)) {
                statement.execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }, executor);
    }

    public CompletableFuture<Set<String>> getKnowledgeForPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            Set<String> keys = new HashSet<>();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("""
                     SELECT knowledge_key FROM player_knowledge WHERE player_uuid = ?
                 """)) {
                statement.setObject(1, uuid);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    keys.add(result.getString("knowledge_key"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return keys;
        }, executor);
    }

    @Override
    public CompletableFuture<List<String>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<String> allKeys = new ArrayList<>();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("""
                     SELECT DISTINCT knowledge_key FROM player_knowledge
                 """)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    allKeys.add(result.getString("knowledge_key"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return allKeys;
        }, executor);
    }

    public CompletableFuture<Void> save(UUID playerId, String key) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("""
                 INSERT IGNORE INTO player_knowledge (player_uuid, knowledge_key, time_unlocked)
                 VALUES (?, ?, ?)
             """)) {
                statement.setObject(1, playerId);
                statement.setString(2, key);
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, executor);
    }

    @Override
    public CompletableFuture<String> getById(UUID uuid) {
        throw new UnsupportedOperationException("Use getKnowledgeForPlayer(UUID) instead.");
    }

    @Override
    public CompletableFuture<String> save(String s) {
        throw new UnsupportedOperationException("Use save(UUID, String) instead.");
    }

    @Override
    public CompletableFuture<Boolean> deleteById(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("""
                     DELETE FROM player_knowledge WHERE player_uuid = ?
                 """)) {
                statement.setObject(1, uuid);
                statement.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }, executor);
    }
}
