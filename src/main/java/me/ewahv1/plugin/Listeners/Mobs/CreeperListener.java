package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffectType;
import me.ewahv1.plugin.Main;

public class CreeperListener implements Listener {

    private Main plugin;
    private Creeper lastExplodedCreeper;

    public CreeperListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Creeper) {
            ((Creeper) entity).setMaxFuseTicks((int)(((Creeper) entity).getMaxFuseTicks() * 0.5));
        }
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            lastExplodedCreeper = (Creeper) event.getEntity();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    for (Entity entity : event.getLocation().getWorld().getEntities()) {
                        if (entity instanceof AreaEffectCloud && entity.getLocation().distance(event.getLocation()) < 6.0D) {
                            entity.remove();
                        }
                    }
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event) {
        if (event.getEntity().hasCustomEffect(PotionEffectType.SPEED)) {
            for (LivingEntity entity : event.getAffectedEntities()) {
                if (entity.getLocation().distance(lastExplodedCreeper.getLocation()) < 6.0D) {
                    entity.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }
    }
}
