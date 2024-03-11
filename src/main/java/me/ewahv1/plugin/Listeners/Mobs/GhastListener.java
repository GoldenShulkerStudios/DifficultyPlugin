package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class GhastListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Ghast) {
            ((Ghast) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(((Ghast) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 1.5);
        }
    }
}
