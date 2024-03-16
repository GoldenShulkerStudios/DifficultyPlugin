package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PiglinBruteListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof PiglinBrute) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Spawn FROM piglinbrutesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int spawn = rs.getInt("Spawn");
                    for (int i = 1; i < spawn; i++) {
                        entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
