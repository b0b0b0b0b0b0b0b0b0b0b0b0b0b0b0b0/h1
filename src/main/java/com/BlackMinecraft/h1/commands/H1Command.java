package com.BlackMinecraft.h1.commands;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.H1;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
public class H1Command implements CommandExecutor {
    private final LifeManager lifeManager;
    private final MessagesManager messagesManager;
    private final H1 plugin;
    private static final int MAX_LIVES = 10;
    public H1Command(LifeManager lifeManager, MessagesManager messagesManager, H1 plugin) {
        this.lifeManager = lifeManager;
        this.messagesManager = messagesManager;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("h1.give")) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
                return true;
            }
            String playerName = args[1];
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("number.format.error")
                        .replace("%input%", args[2]));
                return true;
            }
            Player target = Bukkit.getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("player.not.found"));
                return true;
            }
            String uuid = target.getUniqueId().toString();
            int currentLives = lifeManager.getLives(uuid);
            if (currentLives >= MAX_LIVES) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("give.max")
                        .replace("%max%", String.valueOf(MAX_LIVES)));
                return true;
            }
            int possibleToGive = MAX_LIVES - currentLives;
            if (currentLives + amount > MAX_LIVES) {
                sender.sendMessage(ChatColor.YELLOW + messagesManager.getMessage("give.limited")
                        .replace("%available%", String.valueOf(possibleToGive))
                        .replace("%max%", String.valueOf(MAX_LIVES)));
                amount = possibleToGive;
            }
            lifeManager.addLives(uuid, amount);
            sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success")
                    .replace("%player%", target.getName())
                    .replace("%amount%", String.valueOf(amount)));
            target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.received")
                    .replace("%amount%", String.valueOf(amount)));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("h1.reload")) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
                return true;
            }
            plugin.reloadConfig();
            plugin.getConfigManager().loadConfig();
            plugin.getMessagesManager().reloadMessages();
            sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("reload.success"));
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.unknown"));
            return true;
        }
    }
}
