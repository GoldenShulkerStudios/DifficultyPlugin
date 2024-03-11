package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.WitherSkeleton;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class WitherSkeletonListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof WitherSkeleton) {
            WitherSkeleton wither = (WitherSkeleton) entity;
            ItemStack sword = new ItemStack(Material.DIAMOND_AXE);
            sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            wither.getEquipment().setItemInMainHand(sword);
            wither.getEquipment().setItemInMainHandDropChance(0);
        }
    }
}
