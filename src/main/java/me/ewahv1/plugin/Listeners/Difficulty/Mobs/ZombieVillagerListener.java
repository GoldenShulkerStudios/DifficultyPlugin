package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class ZombieVillagerListener implements Listener {

    private final DatabaseConnection connection;

    public ZombieVillagerListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE_VILLAGER) {
            ZombieVillager zombieVillager = (ZombieVillager) event.getEntity();
            loadZombieVillagerSettingsAsync(zombieVillager);
        }
    }

    private void loadZombieVillagerSettingsAsync(ZombieVillager zombieVillager) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT AxeMaterial, Sharpness FROM diff_zombievillager_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new int[]{rs.getInt("AxeMaterial"), rs.getInt("Sharpness")};
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
                ItemStack axe = new ItemStack(materials[axeMaterial - 1]);
                if (sharpness > 0) {
                    axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                }
                zombieVillager.getEquipment().setItemInMainHand(axe);
            }
        });
    }
}
