package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreeperListener implements Listener {

    private Creeper lastExplodedCreeper;
    private Plugin plugin;

    public CreeperListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Fuse FROM diff_creeper_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int fuse = rs.getInt("Fuse");
                    creeper.setMaxFuseTicks((int) (fuse * 10));
                }
            } catch (SQLException e) {
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
            }, 1L);
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
