package me.ewahv1.plugin.Listeners;

import me.ewahv1.plugin.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DayListener {
    private static int currentDay;

    public static CompletableFuture<Integer> getCurrentDayAsync(DatabaseConnection databaseConnection) {
        return databaseConnection.getConnectionAsync().thenApplyAsync(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("SELECT `Dia Actual` FROM settings WHERE ID = 1");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentDay = rs.getInt("Dia Actual");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return currentDay;
        });
    }
}
