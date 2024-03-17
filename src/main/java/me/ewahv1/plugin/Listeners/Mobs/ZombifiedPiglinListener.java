package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ZombifiedPiglinListener implements Listener {
    @EventHandler
    public void onZombifiedPiglinSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof PiglinAbstract) {
            PiglinAbstract piglin = (PiglinAbstract) event.getEntity();
            try {
                PreparedStatement psSpeed = Connection.getConnection().prepareStatement("SELECT Speed FROM zombifiedpiglinsettings WHERE ID = 1");
                ResultSet rsSpeed = psSpeed.executeQuery();
                if (rsSpeed.next()) {
                    int speed = rsSpeed.getInt("Speed");
                    if (speed > 0) {
                        piglin.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1, false, false));
                    }
                }

                PreparedStatement psWeapon = Connection.getConnection().prepareStatement("SELECT Weapon, FireAspect FROM zombifiedpiglinsettings WHERE ID = 1");
                ResultSet rsWeapon = psWeapon.executeQuery();
                if (rsWeapon.next()) {
                    boolean weapon = rsWeapon.getBoolean("Weapon");
                    int fireAspect = rsWeapon.getInt("FireAspect");
                    if (weapon) {
                        ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
                        if (fireAspect > 0) {
                            blazeRod.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fireAspect);
                        }
                        piglin.getEquipment().setItemInMainHand(blazeRod);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
