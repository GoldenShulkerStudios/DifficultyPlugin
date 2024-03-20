package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SilverfishListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Silverfish) {
            Silverfish silverfish = (Silverfish) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT TripleSpawn, Resistance FROM diff_silverfish_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean tripleSpawn = rs.getBoolean("TripleSpawn");
                    int resistance = rs.getInt("Resistance");
                    if (tripleSpawn) {
                        for (int i = 0; i < 2; i++) {
                            silverfish.getWorld().spawnEntity(silverfish.getLocation(), silverfish.getType());
                        }
                    }
                    if (resistance > 0) {
                        silverfish.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistance - 1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
