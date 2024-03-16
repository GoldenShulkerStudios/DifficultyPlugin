package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HuskListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Husk) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Axe, Sharpness FROM husksettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int axe = rs.getInt("Axe");
                    int sharpness = rs.getInt("Sharpness");
                    Husk husk = (Husk) entity;
                    Material axeMaterial = null;
                    switch (axe) {
                        case 1:
                            axeMaterial = Material.WOODEN_AXE;
                            break;
                        case 2:
                            axeMaterial = Material.STONE_AXE;
                            break;
                        case 3:
                            axeMaterial = Material.IRON_AXE;
                            break;
                        case 4:
                            axeMaterial = Material.DIAMOND_AXE;
                            break;
                        case 5:
                            axeMaterial = Material.NETHERITE_AXE;
                            break;
                    }
                    if (axeMaterial != null) {
                        ItemStack axeItem = new ItemStack(axeMaterial);
                        if (sharpness > 0) {
                            axeItem.addEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                        }
                        husk.getEquipment().setItemInMainHand(axeItem);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
