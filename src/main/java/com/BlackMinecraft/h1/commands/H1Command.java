package com.BlackMinecraft.h1.commands;

import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.util.HealthUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("give") || subCommand.equals("add")) {
            if (validateArgs(sender, args)) {
                return true;
            }
            String playerName = args[1];
            Integer amount = parseAmount(args[2], sender);
            if (amount == null) {
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
            int newLives = currentLives + amount;
            HealthUtil.updatePlayerHealth(target, newLives, messagesManager, false);
            sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success")
                    .replace("%player%", target.getName())
                    .replace("%amount%", String.valueOf(amount)));
            target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.received")
                    .replace("%amount%", String.valueOf(amount)));
            return true;
        } else if (subCommand.equals("set")) {
            if (validateArgs(sender, args)) {
                return true;
            }
            String playerName = args[1];
            Integer newLives = parseAmount(args[2], sender);
            if (newLives == null) {
                return true;
            }
            if (newLives > MAX_LIVES) {
                newLives = MAX_LIVES;
            }
            Player target = Bukkit.getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("player.not.found"));
                return true;
            }
            String uuid = target.getUniqueId().toString();
            lifeManager.setLives(uuid, newLives);
            HealthUtil.updatePlayerHealth(target, newLives, messagesManager, false);
            sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success")
                    .replace("%player%", target.getName())
                    .replace("%amount%", String.valueOf(newLives)));
            target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("life.info")
                    .replace("%lives%", String.valueOf(newLives)));
            return true;
        } else if (subCommand.equals("reload")) {
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
    private boolean validateArgs(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
            return true;
        }
        return false;
    }
    private Integer parseAmount(String input, CommandSender sender) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + messagesManager.getMessage("number.format.error")
                    .replace("%input%", input));
            return null;
        }
    }
}
