package com.BlackMinecraft.h1;
import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.BlackMinecraft.h1.commands.H1Command;
import com.BlackMinecraft.h1.config.ConfigManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.database.DatabaseManager;
import com.BlackMinecraft.h1.listeners.PlayerListener;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.placeholders.H1PlaceholderExpansion;
public final class H1 extends JavaPlugin {

	private static H1 instance;
	private static ConfigManager configManager;
	private static MessagesManager messagesManager;
	private static DatabaseManager databaseManager;
	private static LifeManager lifeManager;

	@Override
	public void onEnable() {
		instance = this;

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		final File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			saveResource("config.yml", false);
		}

		final File messagesFile = new File(getDataFolder(), "messages.yml");
		if (!messagesFile.exists()) {
			saveResource("messages.yml", false);
		}

		configManager = new ConfigManager(this);
		configManager.loadConfig();
		messagesManager = new MessagesManager(this);
		messagesManager.loadMessages();
		databaseManager = new DatabaseManager(this);
		databaseManager.setupDatabase();
		lifeManager = new LifeManager(databaseManager);

		getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		if (getCommand("h1") != null) {
			getCommand("h1").setExecutor(new H1Command(lifeManager, messagesManager, this));
		}

		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new H1PlaceholderExpansion().register();
			getLogger().info("H1 PlaceholderAPI expansion registered.");
		} else {
			getLogger().warning("PlaceholderAPI не найден! Плейсхолдеры H1 не будут работать.");
		}

	}

	@Override
	public void onDisable() {
		getLogger().info("Плагин H1 выключается. Закрытие соединения с базой данных...");
		if (databaseManager != null) {
			databaseManager.close();
		}
	}

	public static H1 getInstance() { return instance; }
	public static ConfigManager getConfigManager() { return configManager; }
	public static MessagesManager getMessagesManager() { return messagesManager; }
	public static LifeManager getLifeManager() { return lifeManager; }
}
