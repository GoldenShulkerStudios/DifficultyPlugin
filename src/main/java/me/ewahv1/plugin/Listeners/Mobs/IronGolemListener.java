package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IronGolemListener implements Listener {
    @EventHandler
    public void onIronGolemSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof IronGolem) {
            IronGolem ironGolem = (IronGolem) event.getEntity();
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT FireResistance FROM irongolemsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int fireResistance = rs.getInt("FireResistance");
                    if (fireResistance == 1) {
                        ironGolem.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
