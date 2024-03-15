package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.entity.Pillager;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PillagerListener implements Listener {
    @EventHandler
    public void onPillagerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Pillager) {
            Pillager pillager = (Pillager) event.getEntity();
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setCritical(true); // Critical damage
                arrow.setDamage(arrow.getDamage() * 2); // Double damage
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 2), true); // Instant Damage 2
            }
            pillager.getEquipment().setItemInMainHandDropChance(0); // No drop for the crossbow
            pillager.getEquipment().setItemInOffHandDropChance(0); // No drop for the arrows
        }
    }
}
