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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class CreeperListener implements Listener {

    private Creeper lastExplodedCreeper;
    private final Plugin plugin;
    private final DatabaseConnection connection;

    public CreeperListener(Plugin plugin, DatabaseConnection connection) {
        this.plugin = plugin;
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            loadCreeperSettingsAsync(creeper);
        }
    }

    private void loadCreeperSettingsAsync(Creeper creeper) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Fuse FROM diff_creeper_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("Fuse");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).thenAccept(fuse -> {
            if (fuse != null) {
                creeper.setMaxFuseTicks(fuse * 10);
            }
        });
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            lastExplodedCreeper = (Creeper) event.getEntity();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (Entity entity : event.getLocation().getWorld().getEntities()) {
                    if (entity instanceof AreaEffectCloud && entity.getLocation().distance(event.getLocation()) < 6.0D) {
                        entity.remove();
                    }
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event) {
        if (event.getEntity().hasCustomEffect(PotionEffectType.SPEED)) {
            for (LivingEntity entity : event.getAffectedEntities()) {
                if (lastExplodedCreeper != null && entity.getLocation().distance(lastExplodedCreeper.getLocation()) < 6.0D) {
                    entity.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }
    }
}
