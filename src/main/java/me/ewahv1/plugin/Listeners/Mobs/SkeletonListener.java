package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class SkeletonListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Skeleton) {
            ((Skeleton) entity).getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
            ((Skeleton) entity).getEquipment().getItemInMainHand().addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            ((Skeleton) entity).getEquipment().setItemInMainHandDropChance(0);
        }
    }
}
