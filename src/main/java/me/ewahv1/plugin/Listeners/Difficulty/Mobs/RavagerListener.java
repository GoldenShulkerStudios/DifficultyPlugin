package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Ravager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class RavagerListener implements Listener {

    private final DatabaseConnection connection;

    public RavagerListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Ravager) {
            Ravager ravager = (Ravager) event.getEntity();
            loadRavagerSettingsAsync(ravager);
        }
    }

    private void loadRavagerSettingsAsync(Ravager ravager) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Resistance, Strength, Speed FROM diff_ravager_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("Resistance"),
                            rs.getInt("Strength"),
                            rs.getInt("Speed")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{0, 0, 0};
        }).thenAccept(settings -> {
            int resistance = (int) settings[0];
            int strength = (int) settings[1];
            int speed = (int) settings[2];

            if (resistance > 0) {
                ravager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistance - 1));
            }

            if (strength > 0) {
                ravager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1));
            }

            if (speed > 0) {
                ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
            }
        });
    }
}
