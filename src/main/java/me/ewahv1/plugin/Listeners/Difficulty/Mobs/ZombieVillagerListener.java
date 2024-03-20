package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZombieVillagerListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE_VILLAGER) {
            ZombieVillager zombieVillager = (ZombieVillager) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT AxeMaterial, Sharpness FROM zombie_villager_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int axeMaterial = rs.getInt("AxeMaterial");
                    int sharpness = rs.getInt("Sharpness");
                    if (axeMaterial > 0) {
                        Material[] materials = {Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
                        ItemStack axe = new ItemStack(materials[axeMaterial - 1]);
                        if (sharpness > 0) {
                            axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                        }
                        zombieVillager.getEquipment().setItemInMainHand(axe);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
