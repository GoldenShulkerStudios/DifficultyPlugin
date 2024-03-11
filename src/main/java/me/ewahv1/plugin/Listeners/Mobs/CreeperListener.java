package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class CreeperListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Creeper) {
            ((Creeper) entity).setMaxFuseTicks((int)(((Creeper) entity).getMaxFuseTicks() * 0.5));
        }
    }
}
