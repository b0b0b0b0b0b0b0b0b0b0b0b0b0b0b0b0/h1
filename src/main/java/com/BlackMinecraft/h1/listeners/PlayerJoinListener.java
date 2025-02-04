package com.BlackMinecraft.h1.listeners;
import com.BlackMinecraft.h1.managers.LifeManager;
import com.BlackMinecraft.h1.config.MessagesManager;
import com.BlackMinecraft.h1.util.HealthUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
public class PlayerJoinListener implements Listener {
    private final LifeManager lifeManager;
    private final MessagesManager messagesManager;

    public PlayerJoinListener(LifeManager lifeManager, MessagesManager messagesManager) {
        this.lifeManager = lifeManager;
        this.messagesManager = messagesManager;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        lifeManager.getLivesAsync(uuid, lives ->
                HealthUtil.updatePlayerHealth(player, lives, messagesManager, false)
        );
    }
}
