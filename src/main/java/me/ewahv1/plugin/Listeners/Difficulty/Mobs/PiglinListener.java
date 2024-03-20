package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PiglinListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Piglin) {
            Piglin piglin = (Piglin) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT SwordMaterial, QuickCharge FROM diff_piglin_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int quickCharge = rs.getInt("QuickCharge");
                    Material material = null;
                    switch (swordMaterial) {
                        case 1: material = Material.WOODEN_SWORD; break;
                        case 2: material = Material.STONE_SWORD; break;
                        case 3: material = Material.GOLDEN_SWORD; break;
                        case 4: material = Material.IRON_SWORD; break;
                        case 5: material = Material.DIAMOND_SWORD; break;
                        case 6: material = Material.NETHERITE_SWORD; break;
                    }
                    if (piglin.getEquipment().getItemInMainHand().getType() == Material.CROSSBOW && quickCharge > 0) {
                        ItemStack crossbow = piglin.getEquipment().getItemInMainHand();
                        crossbow.addEnchantment(Enchantment.QUICK_CHARGE, quickCharge);
                        piglin.getEquipment().setItemInMainHand(crossbow);
                    } else if (material != null) {
                        piglin.getEquipment().setItemInMainHand(new ItemStack(material));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
