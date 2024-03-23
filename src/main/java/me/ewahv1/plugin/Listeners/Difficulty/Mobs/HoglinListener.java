package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HoglinListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Hoglin && event.getEntity() instanceof Player) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Punch FROM diff_hoglin_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int punch = rs.getInt("Punch");
                    Vector velocity = event.getEntity().getVelocity();
                    Vector punchDirection = event.getDamager().getLocation().getDirection().multiply(punch);
                    velocity.add(punchDirection);
                    event.getEntity().setVelocity(velocity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
