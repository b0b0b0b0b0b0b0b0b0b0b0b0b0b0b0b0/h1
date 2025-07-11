package com.BlackMinecraft.h1.config;
import com.BlackMinecraft.h1.H1;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
public class ConfigManager {
    private final H1 plugin;
    private FileConfiguration config;

    public ConfigManager(H1 plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
