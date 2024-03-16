package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HoglinListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Hoglin && event.getEntity() instanceof Player) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Knockback FROM hoglinsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int knockback = rs.getInt("Knockback");
                    Vector velocity = event.getEntity().getVelocity();
                    velocity.setY(knockback);
                    event.getEntity().setVelocity(velocity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
