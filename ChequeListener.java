package org.Axther.contentCore.listeners;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.itemEconomy.ItemEconomy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChequeListener implements Listener {

    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public ChequeListener(ContentCore plugin) {
        this.plugin = plugin;
        this.itemEconomy = plugin.getItemEconomy();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        int chequeAmount = itemEconomy.redeemCheque(item);
        if (chequeAmount > 0) {
            // Consume the cheque
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().removeItem(item);
            }

            // Add the balance to the player
            itemEconomy.deposit(player, chequeAmount);

            player.sendMessage("You have redeemed a cheque for " + chequeAmount + " diamonds!");
            event.setCancelled(true);
        }
    }
}