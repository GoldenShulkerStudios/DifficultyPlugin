package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Vindicator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class VindicatorListener implements Listener {
    @EventHandler
    public void onVindicatorSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Vindicator) {
            Vindicator vindicator = (Vindicator) event.getEntity();
            ItemStack netheriteAxe = new ItemStack(Material.NETHERITE_AXE);
            netheriteAxe.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            vindicator.getEquipment().setItemInMainHand(netheriteAxe);
        }
    }
}
