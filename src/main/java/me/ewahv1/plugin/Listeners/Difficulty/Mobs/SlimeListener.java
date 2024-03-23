package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlimeListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Slime && event.getEntity() instanceof Player) {
            Slime slime = (Slime) event.getDamager();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Punch, Strength FROM diff_slime_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int punch = rs.getInt("Punch");
                    int strength = rs.getInt("Strength");
                    if (punch > 0) {
                        Vector velocity = event.getEntity().getVelocity();
                        Vector punchDirection = slime.getLocation().getDirection().multiply(punch);
                        velocity.add(punchDirection);
                        event.getEntity().setVelocity(velocity);
                    }
                    if (strength > 0) {
                        slime.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength - 1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
