package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class VexListener implements Listener {

    private final DatabaseConnection connection;

    public VexListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.VEX) {
            Vex vex = (Vex) event.getEntity();
            loadVexSettingsAsync(vex);
        }
    }

    private void loadVexSettingsAsync(Vex vex) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT SwordMaterial, Sharpness, Flame FROM diff_vex_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("SwordMaterial"),
                            rs.getInt("Sharpness"),
                            rs.getInt("Flame")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{0, 0, 0};
        }).thenAccept(settings -> {
            int swordMaterial = (int) settings[0];
            int sharpness = (int) settings[1];
            int flame = (int) settings[2];

            if (swordMaterial > 0) {
                Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                if (sharpness > 0) {
                    sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                }
                if (flame > 0) {
                    sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, flame);
                }
                vex.getEquipment().setItemInMainHand(sword);
            }
        });
    }
}
