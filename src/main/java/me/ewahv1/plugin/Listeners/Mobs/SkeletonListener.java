package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.meta.PotionMeta;

public class SkeletonListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Skeleton) {
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2); // Power 5
            ((Skeleton) entity).getEquipment().setItemInMainHand(bow);

            ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 64); // Stack of 64 arrows
            PotionMeta meta = (PotionMeta) arrow.getItemMeta();
            meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 2), true); // Instant Damage 2
            arrow.setItemMeta(meta);
            ((Skeleton) entity).getEquipment().setItemInOffHand(arrow);

            ((Skeleton) entity).getEquipment().setItemInMainHandDropChance(0);
            ((Skeleton) entity).getEquipment().setItemInOffHandDropChance(0);
        }
    }
}
