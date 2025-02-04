package com.BlackMinecraft.h1.listeners;
import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
public class PlayerRespawnListener implements Listener {
    private final H1 plugin;
    private final LifeManager lifeManager;
    private final MessagesManager messagesManager;
    public PlayerRespawnListener(H1 plugin, LifeManager lifeManager, MessagesManager messagesManager) {
        this.plugin = plugin;
        this.lifeManager = lifeManager;
        this.messagesManager = messagesManager;
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        Bukkit.getScheduler().runTask(plugin, () -> {
            int lives = lifeManager.getLives(uuid);
            double newMaxHealth = lives * 2.0;
            if (newMaxHealth < 2.0) {
                newMaxHealth = 2.0;
            }
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
            player.setHealthScaled(true);
            player.setHealthScale(newMaxHealth);
            player.setHealth(newMaxHealth);
            player.sendMessage(messagesManager.getMessage("life.info")
                    .replace("%lives%", String.valueOf(lives)));
        });
    }
}
