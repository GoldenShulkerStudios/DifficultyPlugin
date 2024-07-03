package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class PhantomListener implements Listener {

    private final DatabaseConnection connection;

    public PhantomListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getEntity();
            loadPhantomSettingsAsync(phantom);
        }
    }

    private void loadPhantomSettingsAsync(Phantom phantom) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Size FROM diff_phantom_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("Size");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).thenAccept(size -> {
            if (size > 0) {
                phantom.setSize(size);
            }
        });
    }
}
