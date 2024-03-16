package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhantomListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Phantom) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Size FROM phantomsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int size = rs.getInt("Size");
                    ((Phantom) entity).setSize(size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
