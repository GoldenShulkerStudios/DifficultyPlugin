package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.Connection;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GhastListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            ghast.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200.0D);
            ghast.setHealth(200.0D);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
            if (entityDamageByEntityEvent.getDamager() instanceof Fireball) {
                Fireball fireball = (Fireball) entityDamageByEntityEvent.getDamager();
                if (fireball.getShooter() instanceof Ghast) {
                    try {
                        PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT ExplosionPower FROM ghast_settings WHERE ID = 1");
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            int explosionPower = rs.getInt("ExplosionPower");
                            if (explosionPower > 0) {
                                event.setDamage(event.getDamage() * (explosionPower + 1));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
