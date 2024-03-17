package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MagmaCubeListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof MagmaCube && event.getEntity() instanceof Player) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Knockback, Strength FROM magmacubesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int knockback = rs.getInt("Knockback");
                    int strength = rs.getInt("Strength");
                    MagmaCube magmaCube = (MagmaCube) event.getDamager();
                    if (knockback > 0) {
                        Vector velocity = event.getEntity().getVelocity();
                        Vector knockbackDirection = event.getDamager().getLocation().getDirection().multiply(knockback);
                        velocity.add(knockbackDirection);
                        event.getEntity().setVelocity(velocity);
                    }
                    if (strength > 0) {
                        magmaCube.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
