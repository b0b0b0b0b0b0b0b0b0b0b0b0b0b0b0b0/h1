package com.BlackMinecraft.h1.database;
import com.BlackMinecraft.h1.H1;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.sql.*;
public class DatabaseManager {
    private final H1 plugin;
    private Connection connection;
    public DatabaseManager(H1 plugin) {
        this.plugin = plugin;
    }
    public void setupDatabase() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            FileConfiguration config = plugin.getConfig();
            String dbFileName = config.getString("database.file", "players.db");
            File dbFile = new File(dataFolder, dbFileName);
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_lives (" +
                "uuid TEXT PRIMARY KEY," +
                "lives INTEGER" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
