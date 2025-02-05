package com.BlackMinecraft.h1.listeners;
import com.BlackMinecraft.h1.managers.LifeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
public class PlayerDeathListener implements Listener {
    private final LifeManager lifeManager;
    public PlayerDeathListener(LifeManager lifeManager) {
        this.lifeManager = lifeManager;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String uuid = player.getUniqueId().toString();
        lifeManager.reduceLife(uuid);
    }
}
