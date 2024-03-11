package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AllMobsListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 2);
            monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false, true));
        }
    }
}
