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
    public H1Command(LifeManager lifeManager, MessagesManager messagesManager, H1 plugin) {
        this.lifeManager = lifeManager;
        this.messagesManager = messagesManager;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Используйте: /h1 give <игрок> <кол-во> или /h1 reload");
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("h1.give")) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Использование: /h1 give <игрок> <кол-во>");
                return true;
            }
            String playerName = args[1];
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Неверное число: " + args[2]);
                return true;
            }
            Player target = Bukkit.getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + messagesManager.getMessage("player.not.found"));
                return true;
            }
            String uuid = target.getUniqueId().toString();
            lifeManager.addLives(uuid, amount);
            sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success")
                    .replace("%player%", target.getName())
                    .replace("%amount%", String.valueOf(amount)));
            target.sendMessage(ChatColor.GREEN + "Вам добавили " + amount + " жизней.");
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
            sender.sendMessage(ChatColor.RED + "Неизвестная команда!");
            return true;
        }
    }
}
