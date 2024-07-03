package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Endermite;
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

public class EndermiteListener implements Listener {

    private final DatabaseConnection connection;

    public EndermiteListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Endermite) {
            Endermite endermite = (Endermite) event.getEntity();
            loadEndermiteSettingsAsync(endermite);
        }
    }

    private void loadEndermiteSettingsAsync(Endermite endermite) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Invulnerability, Strength FROM diff_endermite_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Invulnerability");
                    int strength = rs.getInt("Strength");
                    return new EndermiteSettings(invulnerability, strength);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(settings -> {
            if (settings != null) {
                applyEndermiteSettings(endermite, settings);
            }
        });
    }

    private void applyEndermiteSettings(Endermite endermite, EndermiteSettings settings) {
        if (settings.invulnerability) {
            endermite.setInvulnerable(true);
        }
        if (settings.strength > 0) {
            endermite.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, settings.strength - 1));
        }
    }

    private static class EndermiteSettings {
        boolean invulnerability;
        int strength;

        EndermiteSettings(boolean invulnerability, int strength) {
            this.invulnerability = invulnerability;
            this.strength = strength;
        }
    }
}
