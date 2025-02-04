package com.BlackMinecraft.h1;
import com.BlackMinecraft.h1.commands.H1Command;
import com.BlackMinecraft.h1.config.ConfigManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.database.DatabaseManager;
import com.BlackMinecraft.h1.listeners.PlayerDeathListener;
import com.BlackMinecraft.h1.listeners.PlayerJoinListener;
import com.BlackMinecraft.h1.listeners.PlayerRespawnListener;
import com.BlackMinecraft.h1.managers.LifeManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
public final class H1 extends JavaPlugin {
    private static H1 instance;
    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private DatabaseManager databaseManager;
    private LifeManager lifeManager;
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
        File messagesFile = new File(getDataFolder(), "messages.yml");
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
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(lifeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this, lifeManager, messagesManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(lifeManager, messagesManager), this);
        if (getCommand("h1") != null) {
            getCommand("h1").setExecutor(new H1Command(lifeManager, messagesManager, this));
        }
    }
    @Override
    public void onDisable() {
        getLogger().info("Плагин H1 выключается. Закрытие соединения с базой данных...");
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
    public static H1 getInstance() {return instance;}
    public ConfigManager getConfigManager() {return configManager;}
    public MessagesManager getMessagesManager() {return messagesManager;}
    public DatabaseManager getDatabaseManager() {return databaseManager;}
    public LifeManager getLifeManager() {return lifeManager;}
}
