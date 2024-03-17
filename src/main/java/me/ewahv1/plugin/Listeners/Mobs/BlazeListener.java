package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BlazeListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Blaze) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability FROM blazesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ((Blaze) entity).setInvulnerable(rs.getBoolean("Invulnerability"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
