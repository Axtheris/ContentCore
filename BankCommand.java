package org.Axther.contentCore.commands.economy;

import org.Axther.contentCore.ContentCore;
import org.Axther.contentCore.itemEconomy.ItemEconomy;
import org.Axther.contentCore.commands.IContentCoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements IContentCoreCommand {
    private final ContentCore plugin;
    private final ItemEconomy itemEconomy;

    public BankCommand(ContentCore plugin, ItemEconomy itemEconomy) {
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
        int balance = itemEconomy.getBalance(player);
        player.sendMessage("Your current diamond balance: " + balance);
        // Here you would open the GUI for depositing diamonds
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}