package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.Pillager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PillagerListener implements Listener {
    @EventHandler
    public void onPillagerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Pillager) {
            if (event.getProjectile() instanceof Arrow) {
                event.setCancelled(true);
                Arrow arrow = (Arrow) event.getEntity().getWorld().spawnEntity(event.getProjectile().getLocation(), EntityType.ARROW);
                arrow.setShooter(event.getEntity());
                arrow.setVelocity(event.getProjectile().getVelocity());
                arrow.setCritical(true);
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 2), true);
            }
        }
    }
}
