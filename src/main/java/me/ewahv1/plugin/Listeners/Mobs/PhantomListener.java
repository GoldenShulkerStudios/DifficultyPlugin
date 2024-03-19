package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhantomListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Size FROM phantom_settings WHERE ID = ?");
                ps.setInt(1, phantom.getEntityId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int size = rs.getInt("Size");
                    if (size > 0) {
                        phantom.setSize(size);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
