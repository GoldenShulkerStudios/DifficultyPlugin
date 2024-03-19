package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeeListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Bee) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Hostility FROM bee_settings WHERE ID = ?");
                ps.setInt(1, entity.getEntityId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean hostility = rs.getBoolean("Hostility");
                    Bee bee = (Bee) entity;
                    bee.setAnger(hostility ? Integer.MAX_VALUE : 0);
                    if (hostility) {
                        Player nearestPlayer = bee.getWorld().getPlayers().stream()
                                .min((p1, p2) -> (int) (p1.getLocation().distance(bee.getLocation()) - p2.getLocation().distance(bee.getLocation())))
                                .orElse(null);
                        if (nearestPlayer != null) {
                            bee.setTarget(nearestPlayer);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Bee) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Strength FROM bee_settings WHERE ID = ?");
                ps.setInt(1, event.getDamager().getEntityId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int strength = rs.getInt("Strength");
                    if (strength > 0) {
                        ((Bee) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1, false, false));
                    }
                    event.setDamage(event.getDamage() * strength);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
