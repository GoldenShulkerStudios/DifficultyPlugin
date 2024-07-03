package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Blaze;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class BlazeListener implements Listener {

    private final DatabaseConnection connection;

    public BlazeListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Blaze) {
            Blaze blaze = (Blaze) event.getEntity();
            loadBlazeSettingsAsync(blaze);
        }
    }

    private void loadBlazeSettingsAsync(Blaze blaze) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Invulnerability FROM diff_blaze_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("Invulnerability");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // Default invulnerability if not found or error
        }).thenAccept(invulnerability -> {
            if (invulnerability) {
                blaze.setInvulnerable(true);
            }
        });
    }
}
