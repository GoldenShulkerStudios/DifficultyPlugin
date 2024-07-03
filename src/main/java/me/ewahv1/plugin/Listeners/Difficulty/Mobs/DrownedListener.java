package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DrownedListener implements Listener {

    private final DatabaseConnection connection;

    public DrownedListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Drowned) {
            Drowned drowned = (Drowned) entity;
            loadDrownedSettingsAsync(drowned);
        }
    }

    private void loadDrownedSettingsAsync(Drowned drowned) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Trident, Channeling FROM diff_drowned_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    boolean trident = rs.getBoolean("Trident");
                    boolean channeling = rs.getBoolean("Channeling");
                    return new DrownedSettings(trident, channeling);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(settings -> {
            if (settings != null) {
                applyDrownedSettings(drowned, settings);
            }
        });
    }

    private void applyDrownedSettings(Drowned drowned, DrownedSettings settings) {
        if (settings.trident) {
            ItemStack tridentItem = new ItemStack(Material.TRIDENT);
            if (settings.channeling) {
                tridentItem.addEnchantment(Enchantment.CHANNELING, 1);
            }
            drowned.getEquipment().setItemInMainHand(tridentItem);
        }
    }

    private static class DrownedSettings {
        boolean trident;
        boolean channeling;

        DrownedSettings(boolean trident, boolean channeling) {
            this.trident = trident;
            this.channeling = channeling;
        }
    }
}
