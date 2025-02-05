package com.BlackMinecraft.h1.placeholders;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import com.BlackMinecraft.h1.H1;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
public class H1PlaceholderExpansion extends PlaceholderExpansion {

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "h1";
	}

	@Override
	public @NotNull String getAuthor() {
		return "bobobo";
	}

	@Override
	public @NotNull String getVersion() {
		return H1.getInstance().getDescription().getVersion();
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String identifier) {
		if (player == null)  return "";

		if (identifier.equalsIgnoreCase("lives")) {
			return String.valueOf(H1.getLifeManager().getLives(player.getUniqueId().toString()));
		}

		return null;
	}

}
