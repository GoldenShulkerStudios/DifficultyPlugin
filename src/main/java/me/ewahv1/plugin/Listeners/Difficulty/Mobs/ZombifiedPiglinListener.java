package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZombifiedPiglinListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof PigZombie) {
            PigZombie pigZombie = (PigZombie) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Speed, SwordMaterial, FireAspect FROM diff_zombifiedpiglin_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int speed = rs.getInt("Speed");
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int fireAspect = rs.getInt("FireAspect");
                    if (speed > 0) {
                        pigZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
                    }
                    if (swordMaterial > 0) {
                        Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                        ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                        if (fireAspect > 0) {
                            sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fireAspect);
                        }
                        pigZombie.getEquipment().setItemInMainHand(sword);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
