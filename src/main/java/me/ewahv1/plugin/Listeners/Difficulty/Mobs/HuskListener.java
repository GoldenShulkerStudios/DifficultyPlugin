package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Husk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class HuskListener implements Listener {

    private final DatabaseConnection connection;

    public HuskListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Husk) {
            Husk husk = (Husk) event.getEntity();
            loadHuskSettingsAsync(husk);
        }
    }

    private void loadHuskSettingsAsync(Husk husk) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT AxeMaterial, Sharpness FROM diff_husk_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int axeMaterial = rs.getInt("AxeMaterial");
                    int sharpness = rs.getInt("Sharpness");
                    return new int[]{axeMaterial, sharpness};
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new int[]{0, 0};
        }).thenAccept(settings -> {
            int axeMaterial = settings[0];
            int sharpness = settings[1];
            if (axeMaterial > 0) {
                Material[] materials = {Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
                ItemStack axeItem = new ItemStack(materials[axeMaterial - 1]);
                if (sharpness > 0) {
                    axeItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                }
                husk.getEquipment().setItemInMainHand(axeItem);
            }
        });
    }
}
