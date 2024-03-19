package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
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

public class ZombifiedPiglin implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof PigZombie) {
            PigZombie pigZombie = (PigZombie) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Speed, BlazeRod, FireAspect FROM zombifiedpiglin_settings WHERE ID = ?");
                ps.setInt(1, pigZombie.getEntityId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int speed = rs.getInt("Speed");
                    boolean blazeRod = rs.getBoolean("BlazeRod");
                    int fireAspect = rs.getInt("FireAspect");
                    if (speed > 0) {
                        pigZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
                    }
                    if (blazeRod) {
                        ItemStack blazeRodItem = new ItemStack(Material.BLAZE_ROD);
                        blazeRodItem.addEnchantment(Enchantment.FIRE_ASPECT, fireAspect);
                        pigZombie.getEquipment().setItemInMainHand(blazeRodItem);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
