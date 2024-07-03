package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class PiglinListener implements Listener {

    private final DatabaseConnection connection;

    public PiglinListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Piglin) {
            Piglin piglin = (Piglin) event.getEntity();
            loadPiglinSettingsAsync(piglin);
        }
    }

    private void loadPiglinSettingsAsync(Piglin piglin) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT SwordMaterial, QuickCharge FROM diff_piglin_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new int[]{rs.getInt("SwordMaterial"), rs.getInt("QuickCharge")};
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new int[]{0, 0};
        }).thenAccept(settings -> {
            int swordMaterial = settings[0];
            int quickCharge = settings[1];
            if (swordMaterial > 0) {
                Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                if (quickCharge > 0) {
                    sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, quickCharge);
                }
                piglin.getEquipment().setItemInMainHand(sword);
            }
        });
    }
}
