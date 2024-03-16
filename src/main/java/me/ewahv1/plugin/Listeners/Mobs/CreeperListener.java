package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreeperListener implements Listener {

    private JavaPlugin plugin;
    private Creeper lastExplodedCreeper;

    public CreeperListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Creeper) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT ExplosionSpeed FROM creepersettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double explosionSpeed = rs.getDouble("ExplosionSpeed");
                    ((Creeper) entity).setMaxFuseTicks((int)(((Creeper) entity).getMaxFuseTicks() / explosionSpeed));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
