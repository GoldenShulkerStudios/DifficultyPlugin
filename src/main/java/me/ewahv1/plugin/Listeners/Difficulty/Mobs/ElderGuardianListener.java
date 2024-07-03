package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.ElderGuardian;
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

public class ElderGuardianListener implements Listener {

    private final DatabaseConnection connection;

    public ElderGuardianListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ElderGuardian) {
            ElderGuardian elderGuardian = (ElderGuardian) event.getEntity();
            loadElderGuardianSettingsAsync(elderGuardian);
        }
    }

    private void loadElderGuardianSettingsAsync(ElderGuardian elderGuardian) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Resistance FROM diff_elderguardian_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int resistance = rs.getInt("Resistance");
                    return new ElderGuardianSettings(resistance);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(settings -> {
            if (settings != null) {
                applyElderGuardianSettings(elderGuardian, settings);
            }
        });
    }

    private void applyElderGuardianSettings(ElderGuardian elderGuardian, ElderGuardianSettings settings) {
        if (settings.resistance > 0) {
            elderGuardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, settings.resistance - 1));
        }
    }

    private static class ElderGuardianSettings {
        int resistance;

        ElderGuardianSettings(int resistance) {
            this.resistance = resistance;
        }
    }
}
