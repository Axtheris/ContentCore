package org.Axther.contentCore;

import org.Axther.contentCore.commands.IContentCoreCommand;
import org.Axther.contentCore.commands.ReloadCommand;
import org.Axther.contentCore.commands.economy.*;
import org.Axther.contentCore.Economy.ItemEconomy;
import org.Axther.contentCore.listeners.ChequeListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ContentCore extends JavaPlugin implements CommandExecutor, TabCompleter {

    private static final String PERMISSION_PREFIX = "contentcore.";
    private Map<String, IContentCoreCommand> commands;
    private ItemEconomy itemEconomy;

    @Override
    public void onEnable() {
        getLogger().info("ContentCore is being enabled.");

        try {
            initializeEconomy();
            initializeCommands();
            registerCommands();
            registerListeners();
            scheduleInterestTask();
        } catch (Exception e) {
            getLogger().severe("Failed to enable ContentCore: " + e.getMessage());
            e.printStackTrace();
        }

        getLogger().info("ContentCore has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ContentCore has been disabled!");
    }

    private void initializeEconomy() {
        itemEconomy = new ItemEconomy(this);
    }

    private void initializeCommands() {
        commands = new HashMap<>();
        commands.put("reload", new ReloadCommand(this));
        commands.put("bank", new BankCommand(this, itemEconomy));
        commands.put("pay", new PayCommand(this, itemEconomy));
        commands.put("deposit", new DepositCommand(this, itemEconomy));
        commands.put("withdraw", new WithdrawCommand(this, itemEconomy));
        commands.put("cheque", new ChequeCommand(this, itemEconomy));
    }

    private void registerCommands() {
        PluginCommand ccCommand = getCommand("cc");
        if (ccCommand != null) {
            ccCommand.setExecutor(this);
            ccCommand.setTabCompleter(this);
            getLogger().info("Successfully registered 'cc' command.");
        } else {
            getLogger().severe("Failed to register 'cc' command. Make sure it's properly defined in plugin.yml.");
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChequeListener(this), this);
    }

    private void scheduleInterestTask() {
        getServer().getScheduler().runTaskTimer(this, () -> {
            try {
                itemEconomy.applyInterest();
                getLogger().info("Applied interest to all player balances.");
            } catch (Exception e) {
                getLogger().severe("Failed to apply interest: " + e.getMessage());
                e.printStackTrace();
            }
        }, 20 * 60 * 60, 20 * 60 * 60);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cc")) {
            if (args.length > 0) {
                String subCommand = args[0].toLowerCase();
                IContentCoreCommand ccCommand = commands.get(subCommand);
                if (ccCommand != null) {
                    if (sender.hasPermission(PERMISSION_PREFIX + subCommand)) {
                        return ccCommand.execute(sender, command, label, args);
                    } else {
                        sender.sendMessage("You don't have permission to use this command.");
                        return true;
                    }
                } else {
                    sender.sendMessage("Unknown subcommand. Use '/cc' for a list of available commands.");
                }
            } else {
                List<String> availableCommands = new ArrayList<>();
                for (String cmd : commands.keySet()) {
                    if (sender.hasPermission(PERMISSION_PREFIX + cmd)) {
                        availableCommands.add(cmd);
                    }
                }
                sender.sendMessage("Available commands: " + String.join(", ", availableCommands));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("cc")) {
            if (args.length == 1) {
                List<String> availableCommands = new ArrayList<>();
                for (String cmd : commands.keySet()) {
                    if (sender.hasPermission(PERMISSION_PREFIX + cmd) && cmd.startsWith(args[0].toLowerCase())) {
                        availableCommands.add(cmd);
                    }
                }
                return availableCommands;
            } else if (args.length > 1) {
                IContentCoreCommand ccCommand = commands.get(args[0].toLowerCase());
                if (ccCommand != null && sender.hasPermission(PERMISSION_PREFIX + args[0].toLowerCase())) {
                    return ccCommand.tabComplete(sender, command, alias, args);
                }
            }
        }
        return new ArrayList<>();
    }

    // Getter for ItemEconomy
    public ItemEconomy getItemEconomy() {
        return itemEconomy;
    }
}