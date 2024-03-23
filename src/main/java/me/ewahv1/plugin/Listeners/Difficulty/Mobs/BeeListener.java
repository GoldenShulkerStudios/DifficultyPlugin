package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeeListener implements Listener {
 
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Bee) {
            try {
                PreparedStatement psHostility = DatabaseConnection.getConnection().prepareStatement("SELECT Hostility FROM dif_bee_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rsHostility = psHostility.executeQuery();
                PreparedStatement psStrength = DatabaseConnection.getConnection().prepareStatement("SELECT Strength FROM dif_Bee_Settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rsStrength = psStrength.executeQuery(); 

                if (rsHostility.next() && rsStrength.next()) {
                    boolean hostility = rsHostility.getBoolean("Hostility");
                    int strength = rsStrength.getInt("Strength");
                    Bee bee = (Bee) entity;
                    bee.setAnger(hostility ? Integer.MAX_VALUE : 0);
                    if (hostility) {
                        Player nearestPlayer = bee.getWorld().getPlayers().stream()
                                .min((p1, p2) -> (int) (p1.getLocation().distance(bee.getLocation()) - p2.getLocation().distance(bee.getLocation())))
                                .orElse(null);
                        if (nearestPlayer != null) {
                            bee.setTarget(nearestPlayer);
                        }
                    }
                    if (strength > 0) {
                        bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1, false, false));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
