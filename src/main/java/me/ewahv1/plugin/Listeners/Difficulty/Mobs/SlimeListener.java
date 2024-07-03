package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Slime;
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

public class SlimeListener implements Listener {

    private final DatabaseConnection connection;

    public SlimeListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Slime && event.getEntity() instanceof Player) {
            Slime slime = (Slime) event.getDamager();
            loadSlimeSettingsAsync(slime, event);
        }
    }

    private void loadSlimeSettingsAsync(Slime slime, EntityDamageByEntityEvent event) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Punch, Strength FROM diff_slime_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("Punch"),
                            rs.getInt("Strength")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{0, 0};
        }).thenAccept(settings -> {
            int punch = (int) settings[0];
            int strength = (int) settings[1];

            if (punch > 0) {
                Vector velocity = event.getEntity().getVelocity();
                Vector punchDirection = slime.getLocation().getDirection().multiply(punch);
                velocity.add(punchDirection);
                event.getEntity().setVelocity(velocity);
            }

            if (strength > 0) {
                slime.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1, false, false));
            }
        });
    }
}
