package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WitherSkeletonListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.WITHER_SKELETON) {
            WitherSkeleton witherSkeleton = (WitherSkeleton) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT AxeMaterial, Sharpness FROM diff_witherskeleton_settings WHERE ID = " + DayListener.getCurrentDay());
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
                        witherSkeleton.getEquipment().setItemInMainHand(axe);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
