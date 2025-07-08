package com.BlackMinecraft.h1;
import com.BlackMinecraft.h1.commands.H1Command;
import com.BlackMinecraft.h1.config.ConfigManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.database.DatabaseManager;
import com.BlackMinecraft.h1.listeners.PlayerDeathListener;
import com.BlackMinecraft.h1.listeners.PlayerJoinListener;
import com.BlackMinecraft.h1.listeners.PlayerRespawnListener;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.placeholders.H1PlaceholderExpansion;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Objects;

public final class H1 extends JavaPlugin {
    private static H1 instance;
    private static ConfigManager configManager;
    private static MessagesManager messagesManager;
    private DatabaseManager databaseManager;
    private static LifeManager lifeManager;
    @Override
    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        configManager = new ConfigManager(this);
        configManager.loadConfig();
        String lang = configManager.getConfig().getString("language", "ru");
        File langFile = new File(getDataFolder(), "messages_" + lang + ".yml");
        if (!langFile.exists()) {
            saveResource("messages_" + lang + ".yml", false);
        }
        messagesManager = new MessagesManager(this, lang);
        messagesManager.loadMessages();
        databaseManager = new DatabaseManager(this);
        databaseManager.setupDatabase();
        lifeManager = new LifeManager(databaseManager);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(lifeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(lifeManager, messagesManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(lifeManager, messagesManager), this);
        if (getCommand("h1") != null) {
            Objects.requireNonNull(getCommand("h1")).setExecutor(new H1Command(lifeManager, messagesManager, this));
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new H1PlaceholderExpansion(this, getLifeManager()).register();
            getLogger().info("H1 PlaceholderAPI expansion registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found! H1 placeholders will not work.");
        }
    }
    @Override
    public void onDisable() {
        getLogger().info("Closing connection to the database...");
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
    public static H1 getInstance() {return instance;}
    public static ConfigManager getConfigManager() {return configManager;}
    public static MessagesManager getMessagesManager() {return messagesManager;}
    public static LifeManager getLifeManager() {return lifeManager;}
}
