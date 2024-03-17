package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class AllMobsListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Monster) {
            int lightLevel = event.getLocation().getBlock().getLightLevel();
            
            if (lightLevel > 0) {
                event.setCancelled(false);
                
                event.getEntity().getWorld().getPlayers().forEach(player -> player.sendMessage("Un " + event.getEntityType().name() + " ha aparecido en un bloque con nivel de luz " + lightLevel));
            }
        }
    }
}
