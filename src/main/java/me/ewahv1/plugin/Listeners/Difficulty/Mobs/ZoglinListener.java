package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zoglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZoglinListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOGLIN) {
            Zoglin zoglin = (Zoglin) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT DoubleDamage FROM diff_zoglin_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int doubleDamage = rs.getInt("DoubleDamage");
                    if (doubleDamage > 0) {
                        double baseDamage = zoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                        zoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseDamage * doubleDamage);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
