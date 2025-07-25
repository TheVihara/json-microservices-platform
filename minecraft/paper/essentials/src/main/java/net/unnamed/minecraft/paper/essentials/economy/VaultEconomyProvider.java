package net.unnamed.minecraft.paper.essentials.economy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import net.unnamed.minecraft.paper.essentials.api.player.EssentialsPlayer;
import org.bukkit.OfflinePlayer;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VaultEconomyProvider implements Economy {
    PlayerManager playerManager;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Essentials Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double v) {
        return String.format("$%.2f", v);
    }

    @Override
    public String currencyNamePlural() {
        return "$";
    }

    @Override
    public String currencyNameSingular() {
        return "$";
    }

    private EssentialsPlayer getPlayer(OfflinePlayer offlinePlayer) {
        return playerManager.getCache().getByUUID(offlinePlayer.getUniqueId()).orElse(null);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return getPlayer(player) != null;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        EssentialsPlayer p = getPlayer(player);
        return p != null ? p.getBalance() : 0.0;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        EssentialsPlayer p = getPlayer(player);
        return p != null && p.getBalance() >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        EssentialsPlayer p = getPlayer(player);
        if (p == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found");
        }

        if (p.getBalance() < amount) {
            return new EconomyResponse(0, p.getBalance(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        p.setBalance(p.getBalance() - amount);
        return new EconomyResponse(amount, p.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        EssentialsPlayer p = getPlayer(player);
        if (p == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found");
        }

        p.setBalance(p.getBalance() + amount);
        return new EconomyResponse(amount, p.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return hasAccount(player); // Created on join
    }

    // Unsupported or unused methods

    @Override public boolean hasAccount(String name) { return false; }
    @Override public boolean hasAccount(String name, String worldName) { return false; }
    @Override public boolean hasAccount(OfflinePlayer player, String worldName) { return false; }
    @Override public double getBalance(String name) { return 0; }
    @Override public double getBalance(String name, String worldName) { return 0; }
    @Override public double getBalance(OfflinePlayer player, String worldName) { return 0; }
    @Override public boolean has(String name, double amount) { return false; }
    @Override public boolean has(String name, String worldName, double amount) { return false; }
    @Override public boolean has(OfflinePlayer player, String worldName, double amount) { return false; }
    @Override public EconomyResponse withdrawPlayer(String name, double amount) { return null; }
    @Override public EconomyResponse withdrawPlayer(String name, String worldName, double amount) { return null; }
    @Override public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) { return null; }
    @Override public EconomyResponse depositPlayer(String name, double amount) { return null; }
    @Override public EconomyResponse depositPlayer(String name, String worldName, double amount) { return null; }
    @Override public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) { return null; }
    @Override public EconomyResponse createBank(String name, String player) { return null; }
    @Override public EconomyResponse createBank(String name, OfflinePlayer player) { return null; }
    @Override public EconomyResponse deleteBank(String name) { return null; }
    @Override public EconomyResponse bankBalance(String name) { return null; }
    @Override public EconomyResponse bankHas(String name, double amount) { return null; }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return null; }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return null; }
    @Override public EconomyResponse isBankOwner(String name, String player) { return null; }
    @Override public EconomyResponse isBankOwner(String name, OfflinePlayer player) { return null; }
    @Override public EconomyResponse isBankMember(String name, String player) { return null; }
    @Override public EconomyResponse isBankMember(String name, OfflinePlayer player) { return null; }
    @Override public List<String> getBanks() { return List.of(); }
    @Override public boolean createPlayerAccount(String playerName) { return false; }
    @Override public boolean createPlayerAccount(String playerName, String worldName) { return false; }
    @Override public boolean createPlayerAccount(OfflinePlayer player, String worldName) { return false; }
}
