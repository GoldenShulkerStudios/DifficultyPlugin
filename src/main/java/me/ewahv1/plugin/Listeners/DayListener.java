package me.ewahv1.plugin.Listeners;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DayListener {
    private static int currentDay;
    private static DatabaseConnection connection;

    public static void init(JavaPlugin plugin, DatabaseConnection dbConnection) {
        connection = dbConnection;
        loadCurrentDayAsync().thenAccept(day -> {
            currentDay = day;
            plugin.getLogger().info("El día actual es: " + currentDay);
        });
    }

    public static int getCurrentDay() {
        return currentDay;
    }

    public static CompletableFuture<Integer> loadCurrentDayAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT CurrentDay FROM day_settings WHERE ID = 1")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("CurrentDay");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0; // default day if not found or error
        });
    }
}
