package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    }
}
