package org.Axther.contentCore.commands.economy;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.Economy.ItemEconomy;
import org.Axther.contentCore.commands.IContentCoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DepositCommand implements IContentCoreCommand {
    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public DepositCommand(ContentCore plugin, ItemEconomy itemEconomy) {
        this.plugin = plugin;
        this.itemEconomy = itemEconomy;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        int diamondsToDeposit = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                diamondsToDeposit += item.getAmount();
            }
        }

        if (diamondsToDeposit > 0) {
            player.getInventory().remove(Material.DIAMOND);
            itemEconomy.deposit(player, diamondsToDeposit);
            player.sendMessage("Deposited " + diamondsToDeposit + " diamonds into your account.");
        } else {
            player.sendMessage("You don't have any diamonds to deposit.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}