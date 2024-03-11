package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class VexListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Vex) {
            ((Vex) entity).getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            ((Vex) entity).getEquipment().getItemInMainHand().addEnchantment(Enchantment.DAMAGE_ALL, 2);
            ((Vex) entity).getEquipment().setItemInMainHandDropChance(0);
        }
    }
}
