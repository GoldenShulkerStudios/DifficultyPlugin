package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Trident, Channeling FROM drownedsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean trident = rs.getBoolean("Trident");
                    int channeling = rs.getInt("Channeling");
                    Drowned drowned = (Drowned) entity;
                    if (trident) {
                        ItemStack tridentItem = new ItemStack(Material.TRIDENT);
                        if (channeling > 0) {
                            tridentItem.addEnchantment(Enchantment.CHANNELING, channeling);
                        }
                        drowned.getEquipment().setItemInMainHand(tridentItem);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
