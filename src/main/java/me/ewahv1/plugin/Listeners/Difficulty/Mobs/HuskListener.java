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
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Axe, Sharpness FROM diff_husk_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int axe = rs.getInt("Axe");
                    int sharpness = rs.getInt("Sharpness");
                    Material material = null;
                    switch (axe) {
                        case 1: material = Material.WOODEN_AXE; break;
                        case 2: material = Material.STONE_AXE; break;
                        case 3: material = Material.GOLDEN_AXE; break;
                        case 4: material = Material.IRON_AXE; break;
                        case 5: material = Material.DIAMOND_AXE; break;
                        case 6: material = Material.NETHERITE_AXE; break;
                    }
                    if (material != null) {
                        ItemStack axeItem = new ItemStack(material);
                        if (sharpness > 0) {
                            axeItem.addEnchantment(Enchantment.DAMAGE_ALL, sharpness);
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
