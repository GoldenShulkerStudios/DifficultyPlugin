package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GhastListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
            if (entityDamageByEntityEvent.getDamager() instanceof Fireball) {
                Fireball fireball = (Fireball) entityDamageByEntityEvent.getDamager();
                if (fireball.getShooter() instanceof Ghast) {
                    try {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT ExplosionPower FROM diff_ghast_settings WHERE ID = " + DayListener.getCurrentDay());
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            int explosionPower = rs.getInt("ExplosionPower");
                            if (explosionPower > 0) {
                                event.setDamage(event.getDamage() * (explosionPower + 1));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
