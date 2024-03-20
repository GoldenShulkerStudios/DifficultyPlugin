package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Blaze;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlazeListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Blaze) {
            Blaze blaze = (Blaze) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability FROM blaze_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Invulnerability");
                    if (invulnerability) {
                        blaze.setInvulnerable(true);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
