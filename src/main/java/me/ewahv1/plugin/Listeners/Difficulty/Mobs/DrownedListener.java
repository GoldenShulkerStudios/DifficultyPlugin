package me.ewahv1.plugin.Listeners.Difficulty.Mobs;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DrownedListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Drowned) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT Trident, Channeling FROM diff_drowned_settings WHERE ID = " + DayListener.getCurrentDay());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean trident = rs.getBoolean("Trident");
                    boolean channeling = rs.getBoolean("Channeling");
                    Drowned drowned = (Drowned) entity;
                    if (trident) {
                        ItemStack tridentItem = new ItemStack(Material.TRIDENT);
                        if (channeling) {
                            tridentItem.addEnchantment(Enchantment.CHANNELING, 1);
                        }
                        drowned.getEquipment().setItemInMainHand(tridentItem);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
