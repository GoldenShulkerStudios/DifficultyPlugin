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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HuskListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Husk) {
            Husk husk = (Husk) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT AxeMaterial, Sharpness FROM diff_husk_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int axeMaterial = rs.getInt("AxeMaterial");
                    int sharpness = rs.getInt("Sharpness");
                    if (axeMaterial > 0) {
                        Material[] materials = {Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
                        ItemStack axeItem = new ItemStack(materials[axeMaterial - 1]);
                        if (sharpness > 0) {
                            axeItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                        }
                        husk.getEquipment().setItemInMainHand(axeItem);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
