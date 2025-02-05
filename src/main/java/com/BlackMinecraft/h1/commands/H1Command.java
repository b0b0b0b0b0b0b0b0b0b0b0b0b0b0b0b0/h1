package com.BlackMinecraft.h1.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.util.HealthUtil;
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
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
	@NotNull String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
			return true;
		}

		final String subCommand = args[0].toLowerCase();
		switch (subCommand) {
			case "give", "add" -> {
				if (!sender.hasPermission("h1.give")) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
					return true;
				}
				final ParsedArgs parsed = parseTargetAndAmount(sender, args);
				if (parsed == null) return true;
				if (parsed.currentLives >= MAX_LIVES) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("give.max").replace("%max%", String.valueOf(MAX_LIVES)));
					return true;
				}
				final int possibleToGive = MAX_LIVES - parsed.currentLives;
				int amount = parsed.amount;
				if (parsed.currentLives + amount > MAX_LIVES) {
					sender.sendMessage(ChatColor.YELLOW + messagesManager.getMessage("give.limited").replace("%available%", String.valueOf(possibleToGive)).replace("%max%", String.valueOf(MAX_LIVES)));
					amount = possibleToGive;
				}
				lifeManager.addLives(parsed.uuid, amount);
				final int newLives = parsed.currentLives + amount;
				HealthUtil.updatePlayerHealth(parsed.target, newLives, messagesManager, false);
				sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success").replace("%player%", parsed.target.getName()).replace("%amount%", String.valueOf(amount)));
				parsed.target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.received").replace("%amount%", String.valueOf(amount)));
				return true;
			}
			case "set" -> {
				if (!sender.hasPermission("h1.set")) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
					return true;
				}
				final ParsedArgs parsed = parseTargetAndAmount(sender, args);
				if (parsed == null) return true;
				int newLives = parsed.amount;
				if (newLives > MAX_LIVES) {
					newLives = MAX_LIVES;
				}
				lifeManager.setLives(parsed.uuid, newLives);
				HealthUtil.updatePlayerHealth(parsed.target, newLives, messagesManager, false);
				sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("give.success").replace("%player%", parsed.target.getName()).replace("%amount%", String.valueOf(newLives)));
				parsed.target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("life.info").replace("%lives%", String.valueOf(newLives)));
				return true;
			}
			case "remove" -> {
				if (!sender.hasPermission("h1.remove")) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
					return true;
				}
				final ParsedArgs parsed = parseTargetAndAmount(sender, args);
				if (parsed == null) return true;
				int newLives = parsed.currentLives - parsed.amount;
				if (newLives < 0) {
					newLives = 0;
				}
				lifeManager.setLives(parsed.uuid, newLives);
				HealthUtil.updatePlayerHealth(parsed.target, newLives, messagesManager, false);
				sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("remove.success").replace("%player%", parsed.target.getName()).replace("%amount%", String.valueOf(parsed.amount)));
				parsed.target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("life.info").replace("%lives%", String.valueOf(newLives)));
				return true;
			}
			case "recover" -> {
				if (!sender.hasPermission("h1.recover")) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
					return true;
				}
				final Player target = getTargetPlayer(sender, args, 1);
				if (target == null) return true;
				final String uuid = target.getUniqueId().toString();
				final int newLives = MAX_LIVES;
				lifeManager.setLives(uuid, newLives);
				HealthUtil.updatePlayerHealth(target, newLives, messagesManager, false);
				sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("recover.success").replace("%player%", target.getName()));
				target.sendMessage(ChatColor.GREEN + messagesManager.getMessage("life.info").replace("%lives%", String.valueOf(newLives)));
				return true;
			}
			case "reload" -> {
				if (!sender.hasPermission("h1.reload")) {
					sender.sendMessage(ChatColor.RED + messagesManager.getMessage("no.permission"));
					return true;
				}
				plugin.reloadConfig();
				H1.getConfigManager().loadConfig();
				H1.getMessagesManager().reloadMessages();
				sender.sendMessage(ChatColor.GREEN + messagesManager.getMessage("reload.success"));
				return true;
			}
			default -> {
				sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.unknown"));
				return true;
			}
		}
	}

	private static class ParsedArgs {
		final Player target;
		final int amount;
		final String uuid;
		final int currentLives;

		ParsedArgs(Player target, int amount, String uuid, int currentLives) {
			this.target = target;
			this.amount = amount;
			this.uuid = uuid;
			this.currentLives = currentLives;
		}
	}

	private ParsedArgs parseTargetAndAmount(CommandSender sender, String[] args) {
		final Player target = getTargetPlayer(sender, args, 1);
		if (target == null) return null;
		final Integer amount = getAmount(sender, args);
		if (amount == null) return null;
		final String uuid = target.getUniqueId().toString();
		final int currentLives = lifeManager.getLives(uuid);
		return new ParsedArgs(target, amount, uuid, currentLives);
	}

	private Player getTargetPlayer(CommandSender sender, String[] args, int index) {
		if (args.length <= index) {
			sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
			return null;
		}
		final Player target = Bukkit.getPlayerExact(args[index]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + messagesManager.getMessage("player.not.found"));
		}
		return target;
	}

	private Player getTargetPlayer(CommandSender sender, String[] args) {
		return getTargetPlayer(sender, args, 1);
	}

	private Integer getAmount(CommandSender sender, String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + messagesManager.getMessage("command.usage"));
			return null;
		}
		try {
			return Integer.parseInt(args[2]);
		} catch (final NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + messagesManager.getMessage("number.format.error")
			.replace("%input%", args[2]));
			return null;
		}
	}

}