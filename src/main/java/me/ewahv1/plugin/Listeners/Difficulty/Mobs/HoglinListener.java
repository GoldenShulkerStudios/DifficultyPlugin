package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class HoglinListener implements Listener {

    private final DatabaseConnection connection;

    public HoglinListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Hoglin && event.getEntity() instanceof Player) {
            loadPunchValueAsync(event);
        }
    }

    private void loadPunchValueAsync(EntityDamageByEntityEvent event) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Punch FROM diff_hoglin_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("Punch");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).thenAccept(punch -> {
            if (punch > 0) {
                Vector velocity = event.getEntity().getVelocity();
                Vector punchDirection = event.getDamager().getLocation().getDirection().multiply(punch);
                velocity.add(punchDirection);
                event.getEntity().setVelocity(velocity);
            }
        });
    }
}
