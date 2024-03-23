package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
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
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT SwordMaterial, QuickCharge FROM diff_piglin_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int quickCharge = rs.getInt("QuickCharge");
                    if (swordMaterial > 0) {
                        Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                        ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                        if (quickCharge > 0) {
                            sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, quickCharge);
                        }
                        piglin.getEquipment().setItemInMainHand(sword);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
