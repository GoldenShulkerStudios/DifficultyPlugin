package me.ewahv1.plugin.Listeners.Mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class AllMobsBuffListener implements Listener {

    private double attackDamageMultiplier = 1.0;
    private int speedLevel = 0;
    private int strengthLevel = 0;
    private int resistanceLevel = 0;
    private int regenerationLevel = 0;
    private double invisibilityChance = 0.01; // 1% chance

    public void setAttackDamageMultiplier(double multiplier) {
        this.attackDamageMultiplier = multiplier;
    }

    public void setSpeedLevel(int level) {
        this.speedLevel = level;
    }

    public void setStrengthLevel(int level) {
        this.strengthLevel = level;
    }

    public void setResistanceLevel(int level) {
        this.resistanceLevel = level;
    }

    public void setRegenerationLevel(int level) {
        this.regenerationLevel = level;
    }

    public void setInvisibilityChance(double chance) {
        this.invisibilityChance = chance;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * attackDamageMultiplier);
            if (speedLevel > 0) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel, true, false, true));
            }
            if (strengthLevel > 0) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strengthLevel, true, false, true));
            }
            if (resistanceLevel > 0) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistanceLevel, true, false, true));
            }
            if (regenerationLevel > 0) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenerationLevel, true, false, true));
            }
            if (new Random().nextDouble() < invisibilityChance) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }
        }
    }
}
