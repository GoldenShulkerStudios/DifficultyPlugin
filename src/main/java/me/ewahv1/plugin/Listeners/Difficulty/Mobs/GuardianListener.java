package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Guardian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class GuardianListener implements Listener {

    private final DatabaseConnection connection;

    public GuardianListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Guardian) {
            Guardian guardian = (Guardian) event.getEntity();
            loadGuardianSettingsAsync(guardian);
        }
    }

    private void loadGuardianSettingsAsync(Guardian guardian) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Resistance FROM diff_guardian_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("Resistance");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).thenAccept(resistance -> {
            if (resistance > 0) {
                guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistance - 1));
            }
        });
    }
}
