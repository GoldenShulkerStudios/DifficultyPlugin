package me.ewahv1.plugin.Listeners;

import me.ewahv1.plugin.Database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DayListener {
    private static int currentDay;

    public static int getCurrentDay() {
        return currentDay;
    }

    public static void updateCurrentDay() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `Dia Actual` FROM settings WHERE ID = 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentDay = rs.getInt("Dia Actual");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
