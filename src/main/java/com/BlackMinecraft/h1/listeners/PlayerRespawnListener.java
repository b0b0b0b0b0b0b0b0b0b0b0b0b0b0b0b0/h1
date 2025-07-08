package com.BlackMinecraft.h1.listeners;
import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.config.ConfigManager;
import com.BlackMinecraft.h1.util.HealthUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
public class PlayerRespawnListener implements Listener {
    private final LifeManager lifeManager;
    private final MessagesManager messagesManager;
    private final ConfigManager configManager;
    public PlayerRespawnListener(LifeManager lifeManager, MessagesManager messagesManager) {
        this.lifeManager = lifeManager;
        this.messagesManager = messagesManager;
        this.configManager = H1.getConfigManager();
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        int lives = lifeManager.getLives(uuid);
        HealthUtil.updatePlayerHealth(player, lives, messagesManager);
        if (lives == 0) {
            String cmd = configManager.getConfig().getString("lives.command");
            if (cmd != null && !cmd.isEmpty()) {
                cmd = cmd.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
    }
}
