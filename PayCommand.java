package org.Axther.contentCore.commands.economy;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.itemEconomy.ItemEconomy;
import org.Axther.contentCore.commands.IContentCoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements IContentCoreCommand {
    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public PayCommand(ContentCore plugin, ItemEconomy itemEconomy) {
        this.plugin = plugin;
        this.itemEconomy = itemEconomy;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("Usage: /cc pay <player> <amount>");
            return true;
        }

        Player from = (Player) sender;
        Player to = plugin.getServer().getPlayer(args[1]);
        if (to == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount.");
            return true;
        }

        if (itemEconomy.transfer(from, to, amount)) {
            sender.sendMessage("Successfully paid " + amount + " diamonds to " + to.getName());
            to.sendMessage("You received " + amount + " diamonds from " + from.getName());
        } else {
            sender.sendMessage("Payment failed. Insufficient balance.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return null; // Return null to get a list of online players
        }
        return new ArrayList<>();
    }
}