package org.Axther.contentCore.commands.economy;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.itemEconomy.ItemEconomy;
import org.Axther.contentCore.commands.IContentCoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChequeCommand implements IContentCoreCommand {
    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public ChequeCommand(ContentCore plugin, ItemEconomy itemEconomy) {
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
            sender.sendMessage("Usage: /cc cheque <amount>");
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
            ItemStack cheque = itemEconomy.createCheque(amount);
            player.getInventory().addItem(cheque);
            player.sendMessage("Created a cheque for " + amount + " diamonds.");
        } else {
            player.sendMessage("Failed to create cheque. Insufficient balance.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}