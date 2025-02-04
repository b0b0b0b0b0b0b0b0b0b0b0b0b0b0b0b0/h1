package com.BlackMinecraft.h1.config;

import com.BlackMinecraft.h1.H1;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {
    private final H1 plugin;
    private FileConfiguration config;
    private final File configFile;

    public ConfigManager(H1 plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void loadConfig() {
        // Если файл не существует, он уже должен быть скопирован в H1.onEnable()
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
