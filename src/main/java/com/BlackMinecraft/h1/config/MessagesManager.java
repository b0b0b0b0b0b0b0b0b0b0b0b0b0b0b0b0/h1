package com.BlackMinecraft.h1.config;
import com.BlackMinecraft.h1.H1;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
public class MessagesManager {
    private FileConfiguration messagesConfig;
    private final File messagesFile;
    public MessagesManager(H1 plugin) {
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
    }
    public void loadMessages() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }
    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
    public String getMessage(String path) {
        String prefix = messagesConfig.getString("prefix", "");
        return prefix + messagesConfig.getString(path, path);
    }
    public void reloadMessages() {
        loadMessages();
    }
}
