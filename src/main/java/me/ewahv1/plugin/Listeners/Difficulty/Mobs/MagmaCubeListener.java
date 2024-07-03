package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class MagmaCubeListener implements Listener {

    private final DatabaseConnection connection;

    public MagmaCubeListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof MagmaCube && event.getEntity() instanceof Player) {
            loadMagmaCubeSettingsAsync((MagmaCube) event.getDamager(), (Player) event.getEntity());
        }
    }

    private void loadMagmaCubeSettingsAsync(MagmaCube magmaCube, Player player) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Punch, Strength FROM diff_magmacube_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int punch = rs.getInt("Punch");
                    int strength = rs.getInt("Strength");
                    return new MagmaCubeSettings(punch, strength);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new MagmaCubeSettings(0, 0);
        }).thenAccept(settings -> {
            if (settings.punch > 0) {
                Vector velocity = player.getVelocity();
                Vector punchDirection = magmaCube.getLocation().getDirection().multiply(settings.punch);
                velocity.add(punchDirection);
                player.setVelocity(velocity);
            }
            if (settings.strength > 0) {
                magmaCube.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, settings.strength - 1, false, false));
            }
        });
    }

    private static class MagmaCubeSettings {
        final int punch;
        final int strength;

        MagmaCubeSettings(int punch, int strength) {
            this.punch = punch;
            this.strength = strength;
        }
    }
}
