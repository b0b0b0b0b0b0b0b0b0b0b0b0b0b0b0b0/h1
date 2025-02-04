package com.BlackMinecraft.h1.database;
import com.BlackMinecraft.h1.H1;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseManager {
    private final H1 plugin;
    private HikariDataSource dataSource;
    public DatabaseManager(H1 plugin) {
        this.plugin = plugin;
    }
    public void setupDatabase() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        FileConfiguration config = plugin.getConfig();
        String dbFileName = config.getString("database.file", "players.db");
        File dbFile = new File(dataFolder, dbFileName);
        String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setPoolName("H1SQLitePool");
        hikariConfig.setConnectionTestQuery("SELECT 1");
        dataSource = new HikariDataSource(hikariConfig);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::createTable);
    }
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_lives (" +
                "uuid TEXT PRIMARY KEY," +
                "lives INTEGER" +
                ");";
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
