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
            ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
            diamondSword.addEnchantment(Enchantment.FIRE_ASPECT, 2); // Flame 2
            diamondSword.addEnchantment(Enchantment.DAMAGE_ALL, 3); // Sharpness 3
            ((Vex) entity).getEquipment().setItemInMainHand(diamondSword);
            ((Vex) entity).getEquipment().setItemInMainHandDropChance(0);
        }
    }
}
