package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ZombieListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Zombie) {
            ((Zombie) entity).getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
            ((Zombie) entity).getEquipment().setItemInMainHandDropChance(0);
        }

        if (entity instanceof Drowned) {
            ItemStack trident = new ItemStack(Material.TRIDENT);
            trident.addEnchantment(Enchantment.CHANNELING, 1);
            ((Drowned) entity).getEquipment().setItemInMainHand(trident);
            ((Drowned) entity).getEquipment().setItemInMainHandDropChance(0.03F);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Drowned && event.getEntity() instanceof Player) {
            Drowned drowned = (Drowned) event.getDamager();
            if (drowned.getEquipment().getItemInMainHand().getType() == Material.TRIDENT) {
                drowned.getWorld().strikeLightning(event.getEntity().getLocation());
            }
        }
    }
}