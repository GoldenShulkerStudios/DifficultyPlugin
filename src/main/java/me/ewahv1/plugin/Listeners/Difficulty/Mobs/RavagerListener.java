package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.entity.Ravager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RavagerListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Ravager) {
            Ravager ravager = (Ravager) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Resistance, Strength, Speed FROM diff_ravager_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int resistance = rs.getInt("Resistance");
                    int strength = rs.getInt("Strength");
                    int speed = rs.getInt("Speed");
                    if (resistance > 0) {
                        ravager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistance - 1));
                    }
                    if (strength > 0) {
                        ravager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1));
                    }
                    if (speed > 0) {
                        ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
