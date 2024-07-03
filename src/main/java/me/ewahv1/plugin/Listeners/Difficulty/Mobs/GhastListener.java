package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class GhastListener implements Listener {

    private final DatabaseConnection connection;

    public GhastListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
            if (entityDamageByEntityEvent.getDamager() instanceof Fireball) {
                Fireball fireball = (Fireball) entityDamageByEntityEvent.getDamager();
                if (fireball.getShooter() instanceof Ghast) {
                    loadGhastSettingsAsync(event);
                }
            }
        }
    }

    private void loadGhastSettingsAsync(EntityDamageEvent event) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT ExplosionPower FROM diff_ghast_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("ExplosionPower");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).thenAccept(explosionPower -> {
            if (explosionPower > 0) {
                event.setDamage(event.getDamage() * (explosionPower + 1));
            }
        });
    }
}
