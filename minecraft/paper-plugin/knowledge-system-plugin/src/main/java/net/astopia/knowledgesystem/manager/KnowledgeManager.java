package net.astopia.knowledgesystem.manager;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.knowledgesystem.config.KnowledgeConfig;
import net.astopia.knowledgesystem.dao.KnowledgeDao;
import net.astopia.knowledgesystem.factory.KnowledgeFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KnowledgeManager {

    Map<String, KnowledgeConfig> knowledgeEntries = new HashMap<>();
    Map<UUID, Set<String>> playerKnowledgeCache = new ConcurrentHashMap<>();

    KnowledgeDao knowledgeDao;

    public KnowledgeManager(Path dataPath, HikariDataSource dataSource) {
        this.knowledgeDao = new KnowledgeDao(dataSource);

        KnowledgeFactory factory = new KnowledgeFactory(dataPath);
        this.knowledgeEntries.putAll(factory.getAllEntries());
    }

    public KnowledgeConfig getKnowledgeEntry(String key) {
        return knowledgeEntries.get(key);
    }

    public List<KnowledgeConfig> getKnowledgeEntries() {
        return List.copyOf(knowledgeEntries.values());
    }

    public void registerKnowledgeEntry(KnowledgeConfig knowledgeConfig) {
        knowledgeEntries.put(knowledgeConfig.getKey(), knowledgeConfig);
    }

    public void unregisterKnowledgeEntry(String key) {
        knowledgeEntries.remove(key);
    }

    public void loadPlayerKnowledge(UUID playerId) {
        knowledgeDao.getKnowledgeForPlayer(playerId).thenAccept(knowledge -> {
            playerKnowledgeCache.put(playerId, knowledge);
        });
    }

    public boolean hasUnlocked(UUID playerId, String key) {
        return playerKnowledgeCache.getOrDefault(playerId, Collections.emptySet()).contains(key);
    }

    public void unlockKnowledge(UUID playerId, String key) {
        playerKnowledgeCache.computeIfAbsent(playerId, __ -> new HashSet<>()).add(key);
        knowledgeDao.save(playerId, key);
    }

    public void unloadPlayer(UUID playerId) {
        playerKnowledgeCache.remove(playerId);
    }
}