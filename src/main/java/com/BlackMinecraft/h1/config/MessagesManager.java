package com.BlackMinecraft.h1.config;
import com.BlackMinecraft.h1.H1;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
public class MessagesManager {
    private FileConfiguration messagesConfig;
    private final File messagesFile;
    private final String language;
    public MessagesManager(H1 plugin, String language) {
        this.language = language;
        this.messagesFile = initLangFile(plugin);
    }
    public String getLanguage() { return language; }

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
    private File initLangFile(H1 plugin) {
        return new File(plugin.getDataFolder(), "messages_" + language + ".yml");
    }
    public void reloadMessages() {
        loadMessages();
    }
}
