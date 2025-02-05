package com.BlackMinecraft.h1.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.util.HealthUtil;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		H1.getLifeManager().reduceLife(event.getPlayer().getUniqueId().toString());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		H1.getLifeManager().getLivesAsync(event.getPlayer().getUniqueId().toString(), lives -> HealthUtil.updatePlayerHealth(event.getPlayer(), lives, H1.getMessagesManager(), false) );
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();

		final int lives = H1.getLifeManager().getLives(player.getUniqueId().toString());
		HealthUtil.updatePlayerHealth(player, lives, H1.getMessagesManager());

		if (lives == 0) {
			String cmd = H1.getConfigManager().getConfig().getString("lives.command");
			if (cmd != null && !cmd.isEmpty()) {
				cmd = cmd.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}

	}

}
