package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class GhastListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            // Duplica la salud del Ghast
            ghast.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200.0D); // Duplica la salud del Ghast
            ghast.setHealth(200.0D); // Asegúrate de que la salud actual del Ghast se establezca en el nuevo máximo
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
            if (entityDamageByEntityEvent.getDamager() instanceof Fireball) {
                Fireball fireball = (Fireball) entityDamageByEntityEvent.getDamager();
                if (fireball.getShooter() instanceof Ghast) {
                    // Duplica el daño de la explosión
                    event.setDamage(event.getDamage() * 5); // Duplica el daño de la explosión
                }
            }
        }
    }
}