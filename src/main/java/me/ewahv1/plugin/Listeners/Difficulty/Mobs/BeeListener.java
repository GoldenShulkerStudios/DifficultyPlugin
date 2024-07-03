package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

public class BeeListener implements Listener {

    private DatabaseConnection connection;

    public BeeListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Bee) {
            loadBeeSettingsAsync((Bee) entity);
        }
    }

    private void loadBeeSettingsAsync(Bee bee) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement psHostility = conn.prepareStatement("SELECT Hostility FROM diff_bee_settings WHERE ID = ?");
                 PreparedStatement psStrength = conn.prepareStatement("SELECT Strength FROM diff_bee_settings WHERE ID = ?")) {

                psHostility.setInt(1, currentDay);
                psStrength.setInt(1, currentDay);

                ResultSet rsHostility = psHostility.executeQuery();
                ResultSet rsStrength = psStrength.executeQuery();

                if (rsHostility.next() && rsStrength.next()) {
                    boolean hostility = rsHostility.getBoolean("Hostility");
                    int strength = rsStrength.getInt("Strength");
                    return new BeeSettings(hostility, strength);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(settings -> {
            if (settings != null) {
                applyBeeSettings(bee, settings);
            }
        });
    }

    private void applyBeeSettings(Bee bee, BeeSettings settings) {
        bee.setAnger(settings.hostility ? Integer.MAX_VALUE : 0);
        if (settings.hostility) {
            Player nearestPlayer = bee.getWorld().getPlayers().stream()
                    .min((p1, p2) -> (int) (p1.getLocation().distance(bee.getLocation()) - p2.getLocation().distance(bee.getLocation())))
                    .orElse(null);
            if (nearestPlayer != null) {
                bee.setTarget(nearestPlayer);
            }
        }
        if (settings.strength > 0) {
            bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, settings.strength - 1, false, false));
        }
    }

    private static class BeeSettings {
        boolean hostility;
        int strength;

        BeeSettings(boolean hostility, int strength) {
            this.hostility = hostility;
            this.strength = strength;
        }
    }
}
