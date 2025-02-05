package com.BlackMinecraft.h1.managers;
import com.BlackMinecraft.h1.H1;
import com.BlackMinecraft.h1.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
public class LifeManager {
    private final DatabaseManager databaseManager;
    private final int defaultLives;
    public LifeManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        FileConfiguration config = H1.getInstance().getConfig();
        this.defaultLives = config.getInt("lives.default", 10);
    }
    public int getLives(String uuid) {
        String sql = "SELECT lives FROM player_lives WHERE uuid = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("lives");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaultLives;
    }
    public void getLivesAsync(String uuid, Consumer<Integer> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(H1.getInstance(), () -> {
            int lives = getLives(uuid);
            Bukkit.getScheduler().runTask(H1.getInstance(), () -> callback.accept(lives));
        });
    }
    public void setLives(String uuid, int lives) {
        if (exists(uuid)) {
            String sql = "UPDATE player_lives SET lives = ? WHERE uuid = ?";
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, lives);
                ps.setString(2, uuid);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "INSERT INTO player_lives(uuid, lives) VALUES (?, ?)";
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, uuid);
                ps.setInt(2, lives);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean exists(String uuid) {
        String sql = "SELECT 1 FROM player_lives WHERE uuid = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void reduceLife(String uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(H1.getInstance(), () -> {
            int lives = getLives(uuid);
            if (lives > 0) {
                setLives(uuid, lives - 1);
            }
        });
    }
    public void addLives(String uuid, int amount) {
        Bukkit.getScheduler().runTaskAsynchronously(H1.getInstance(), () -> {
            int lives = getLives(uuid);
            setLives(uuid, lives + amount);
        });
    }
}
