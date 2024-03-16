package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EndermiteListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Endermite) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability, Strength FROM endermitesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Invulnerability");
                    int strength = rs.getInt("Strength");
                    Endermite endermite = (Endermite) entity;
                    endermite.setInvulnerable(invulnerability);
                    if (strength > 0) {
                        endermite.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
