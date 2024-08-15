package org.Axther.contentCore.commands.economy;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.itemEconomy.ItemEconomy;
import org.Axther.contentCore.commands.IContentCoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class WithdrawCommand implements IContentCoreCommand {
    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public WithdrawCommand(ContentCore plugin, ItemEconomy itemEconomy) {
        this.plugin = plugin;
        this.itemEconomy = itemEconomy;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /cc withdraw <amount>");
            return true;
        }

        Player player = (Player) sender;
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount.");
            return true;
        }

        if (itemEconomy.withdraw(player, amount)) {
            ItemStack diamonds = new ItemStack(Material.DIAMOND, amount);
            player.getInventory().addItem(diamonds);
            player.sendMessage("Withdrew " + amount + " diamonds from your account.");
        } else {
            player.sendMessage("Withdrawal failed. Insufficient balance.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}