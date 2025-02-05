package com.BlackMinecraft.h1.placeholders;

import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.managers.LifeManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
public class H1PlaceholderExpansion extends PlaceholderExpansion {
    private final H1 plugin;
    private final LifeManager lifeManager;
    public H1PlaceholderExpansion(H1 plugin, LifeManager lifeManager) {
        this.plugin = plugin;
        this.lifeManager = lifeManager;
    }
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
        return plugin.getDescription().getVersion();
    }
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        if (identifier.equalsIgnoreCase("lives")) {
            int lives = lifeManager.getLives(player.getUniqueId().toString());
            return String.valueOf(lives);
        }
        return null;
    }
}
