package me.ewahv1.plugin.Listeners.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PillagerListener implements Listener {
    @EventHandler
    public void onPillagerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Pillager) {
            Pillager pillager = (Pillager) event.getEntity();
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                
                try {
                    PreparedStatement psDamage = Connection.getConnection().prepareStatement("SELECT DamageMultiplier FROM pillagersettings WHERE ID = 1");
                    ResultSet rsDamage = psDamage.executeQuery();
                    if (rsDamage.next()) {
                        double damageMultiplier = rsDamage.getDouble("DamageMultiplier");
                        arrow.setDamage(arrow.getDamage() * damageMultiplier);
                    }

                    PreparedStatement psCritical = Connection.getConnection().prepareStatement("SELECT CriticalChance FROM pillagersettings WHERE ID = 1");
                    ResultSet rsCritical = psCritical.executeQuery();
                    if (rsCritical.next()) {
                        double criticalChance = rsCritical.getDouble("CriticalChance");
                        if (Math.random() < criticalChance) {
                            arrow.setCritical(true);
                        }
                    }

                    PreparedStatement psInstantDamage = Connection.getConnection().prepareStatement("SELECT InstantDamage FROM pillagersettings WHERE ID = 1");
                    ResultSet rsInstantDamage = psInstantDamage.executeQuery();
                    if (rsInstantDamage.next()) {
                        int instantDamage = rsInstantDamage.getInt("InstantDamage");
                        arrow.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, instantDamage), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pillager.getEquipment().setItemInMainHandDropChance(0); // No drop for the crossbow
            pillager.getEquipment().setItemInOffHandDropChance(0); // No drop for the arrows
        }
    }
}
