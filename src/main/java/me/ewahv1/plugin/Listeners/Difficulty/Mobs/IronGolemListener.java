package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class IronGolemListener implements Listener {

    private final DatabaseConnection connection;

    public IronGolemListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onIronGolemSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof IronGolem) {
            IronGolem ironGolem = (IronGolem) event.getEntity();
            loadIronGolemSettingsAsync(ironGolem);
        }
    }

    private void loadIronGolemSettingsAsync(IronGolem ironGolem) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT FireResistance FROM diff_irongolem_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    boolean fireResistance = rs.getBoolean("FireResistance");
                    return fireResistance;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).thenAccept(fireResistance -> {
            if (fireResistance) {
                ironGolem.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            }
        });
    }
}
