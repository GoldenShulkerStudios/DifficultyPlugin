package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class EndermanListener implements Listener {

    private final DatabaseConnection connection;

    public EndermanListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Enderman) {
            Enderman enderman = (Enderman) entity;
            loadEndermanSettingsAsync(enderman);
        }
    }

    private void loadEndermanSettingsAsync(Enderman enderman) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Speed, Strength FROM diff_enderman_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int speed = rs.getInt("Speed");
                    int strength = rs.getInt("Strength");
                    return new EndermanSettings(speed, strength);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(settings -> {
            if (settings != null) {
                applyEndermanSettings(enderman, settings);
            }
        });
    }

    private void applyEndermanSettings(Enderman enderman, EndermanSettings settings) {
        if (settings.speed > 0) {
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, settings.speed - 1));
        }
        if (settings.strength > 0) {
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, settings.strength - 1));
        }
    }

    private static class EndermanSettings {
        int speed;
        int strength;

        EndermanSettings(int speed, int strength) {
            this.speed = speed;
            this.strength = strength;
        }
    }
}
