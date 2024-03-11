package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BlazeListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Blaze) {
            ((Blaze) entity).setInvulnerable(true);
        }
    }
}