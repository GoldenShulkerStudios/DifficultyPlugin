package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkeletonListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT PowerBow, ArrowEffectInstantDamage FROM diff_skeleton_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int powerBow = rs.getInt("PowerBow");
                    int arrowEffectInstantDamage = rs.getInt("ArrowEffectInstantDamage");
                    if (powerBow > 0) {
                        ItemStack bow = new ItemStack(Material.BOW, 1);
                        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, powerBow);
                        skeleton.getEquipment().setItemInMainHand(bow);
                    }
                    if (arrowEffectInstantDamage > 0) {
                        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.HARM, Integer.MAX_VALUE, arrowEffectInstantDamage - 1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
