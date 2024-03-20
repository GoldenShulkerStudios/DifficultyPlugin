package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PiglinBruteListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof PiglinBrute) {
            PiglinBrute piglinBrute = (PiglinBrute) event.getEntity();
            if (!piglinBrute.getScoreboardTags().contains("clon")) {
                try {
                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT SpawnQuantity FROM diff_piglinbrute_settings WHERE ID = 1");
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int spawnQuantity = rs.getInt("SpawnQuantity");
                        for (int i = 0; i < spawnQuantity; i++) {
                            PiglinBrute clone = (PiglinBrute) piglinBrute.getWorld().spawnEntity(piglinBrute.getLocation(), piglinBrute.getType());
                            clone.addScoreboardTag("clon");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
