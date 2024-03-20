package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VexListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.VEX) {
            Vex vex = (Vex) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT SwordMaterial, Sharpness, Flame FROM vex_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int sharpness = rs.getInt("Sharpness");
                    int flame = rs.getInt("Flame");
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
