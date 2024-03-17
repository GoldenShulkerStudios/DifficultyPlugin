package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DrownedListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Drowned) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Trident FROM drownedsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean trident = rs.getBoolean("Trident");
                    Drowned drowned = (Drowned) entity;
                    if (trident) {
                        ItemStack tridentItem = new ItemStack(Material.TRIDENT);
                        drowned.getEquipment().setItemInMainHand(tridentItem);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
