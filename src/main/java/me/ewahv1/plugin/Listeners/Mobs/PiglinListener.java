package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PiglinListener implements Listener {
    @EventHandler
    public void onPiglinSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Piglin) {
            Piglin piglin = (Piglin) event.getEntity();
            try {
                PreparedStatement psSword = Connection.getConnection().prepareStatement("SELECT SwordMaterial FROM piglinsettings WHERE ID = 1");
                ResultSet rsSword = psSword.executeQuery();
                if (rsSword.next()) {
                    int swordMaterial = rsSword.getInt("SwordMaterial");
                    Material material;
                    switch (swordMaterial) {
                        case 1:
                            material = Material.WOODEN_SWORD;
                            break;
                        case 2:
                            material = Material.STONE_SWORD;
                            break;
                        case 3:
                            material = Material.IRON_SWORD;
                            break;
                        case 4:
                            material = Material.DIAMOND_SWORD;
                            break;
                        case 5:
                            material = Material.NETHERITE_SWORD;
                            break;
                        default:
                            material = null;
                    }
                    if (material != null) {
                        piglin.getEquipment().setItemInMainHand(new ItemStack(material));
                    }
                }

                PreparedStatement psQuickCharge = Connection.getConnection().prepareStatement("SELECT QuickCharge FROM piglinsettings WHERE ID = 1");
                ResultSet rsQuickCharge = psQuickCharge.executeQuery();
                if (rsQuickCharge.next()) {
                    int quickCharge = rsQuickCharge.getInt("QuickCharge");
                    if (quickCharge > 0) {
                        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
                        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, quickCharge);
                        piglin.getEquipment().setItemInOffHand(crossbow);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}