package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zoglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class ZoglinListener implements Listener {

    private final DatabaseConnection connection;

    public ZoglinListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOGLIN) {
            Zoglin zoglin = (Zoglin) event.getEntity();
            loadZoglinSettingsAsync(zoglin);
        }
    }

    private void loadZoglinSettingsAsync(Zoglin zoglin) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT DoubleDamage FROM diff_zoglin_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("DoubleDamage");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).thenAccept(doubleDamage -> {
            if (doubleDamage > 0) {
                double baseDamage = zoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                zoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseDamage * doubleDamage);
            }
        });
    }
}
