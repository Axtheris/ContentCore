package org.Axther.contentCore.Economy;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemEconomy {
    private final Plugin plugin;
    private final Map<UUID, Integer> balances = new HashMap<>();
    private final Map<UUID, Double> accumulatedInterest = new HashMap<>();

    private static final double INTEREST_RATE = 0.01; // 1% interest rate
    private static final int STACK_SIZE = 64;
    private static final int MAX_INTEREST_PER_STACK = 10;
    private static final NamespacedKey CHEQUE_KEY = new NamespacedKey("contentcore", "cheque_amount");

    public ItemEconomy(Plugin plugin) {
        this.plugin = plugin;
    }

    public int getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0);
    }

    public void setBalance(Player player, int amount) {
        balances.put(player.getUniqueId(), amount);
    }

    public boolean deposit(Player player, int amount) {
        if (amount <= 0) return false;
        UUID playerId = player.getUniqueId();
        balances.put(playerId, getBalance(player) + amount);
        return true;
    }

    public boolean withdraw(Player player, int amount) {
        if (amount <= 0) return false;
        UUID playerId = player.getUniqueId();
        int currentBalance = getBalance(player);
        if (currentBalance < amount) return false;
        balances.put(playerId, currentBalance - amount);
        return true;
    }

    public boolean transfer(Player from, Player to, int amount) {
        if (amount <= 0) return false;
        if (withdraw(from, amount)) {
            deposit(to, amount);
            return true;
        }
        return false;
    }

    public void applyInterest() {
        for (UUID playerId : balances.keySet()) {
            int currentBalance = balances.get(playerId);
            double stackCount = (double) currentBalance / STACK_SIZE;
            double maxInterest = stackCount * MAX_INTEREST_PER_STACK;

            double interestEarned = currentBalance * INTEREST_RATE;
            interestEarned = Math.min(interestEarned, maxInterest);

            double totalInterest = accumulatedInterest.getOrDefault(playerId, 0.0) + interestEarned;
            int wholeInterest = (int) Math.floor(totalInterest);
            double fractionalInterest = totalInterest - wholeInterest;

            if (wholeInterest > 0) {
                balances.put(playerId, currentBalance + wholeInterest);
                accumulatedInterest.put(playerId, fractionalInterest);
            } else {
                accumulatedInterest.put(playerId, totalInterest);
            }
        }
    }

    public ItemStack createCheque(int amount) {
        if (amount <= 0) return null;

        ItemStack cheque = new ItemStack(Material.PAPER);
        ItemMeta meta = cheque.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Diamond Cheque");
            meta.setLore(Arrays.asList("Right-click to redeem", "Value: " + amount + " diamonds"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(CHEQUE_KEY, PersistentDataType.INTEGER, amount);
            cheque.setItemMeta(meta);
        }
        return cheque;
    }

    public int redeemCheque(ItemStack cheque) {
        if (cheque == null || cheque.getType() != Material.PAPER) return 0;
        ItemMeta meta = cheque.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(CHEQUE_KEY, PersistentDataType.INTEGER)) {
            return container.get(CHEQUE_KEY, PersistentDataType.INTEGER);
        }
        return 0;
    }
}