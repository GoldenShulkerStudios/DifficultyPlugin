package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ZombieListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT SwordMaterial, Sharpness, AxeMaterial FROM diff_zombie_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int sharpness = rs.getInt("Sharpness");
                    int axeMaterial = rs.getInt("AxeMaterial");
                    if (swordMaterial > 0) {
                        Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                        ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                        if (sharpness > 0) {
                            sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                        }
                        zombie.getEquipment().setItemInMainHand(sword);
                    }
                    if (axeMaterial > 0) {
                        Material[] materials = {Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
                        ItemStack axe = new ItemStack(materials[axeMaterial - 1]);
                        if (sharpness > 0) {
                            axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                        }
                        zombie.getEquipment().setItemInMainHand(axe);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
