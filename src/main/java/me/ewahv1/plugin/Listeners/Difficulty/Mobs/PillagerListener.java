package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Pillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PillagerListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Pillager) {
            Pillager pillager = (Pillager) event.getEntity();
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT DoubleDamage, CriticPercentage, ArrowEffectInstantDamage, ArrowTier FROM diff_pillager_settings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean doubleDamage = rs.getBoolean("DoubleDamage");
                    int criticPercentage = rs.getInt("CriticPercentage");
                    boolean arrowEffectInstantDamage = rs.getBoolean("ArrowEffectInstantDamage");
                    int arrowTier = rs.getInt("ArrowTier");
                    if (doubleDamage) {
                        pillager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                    }
                    if (criticPercentage > 0) {
                        // Aquí necesitarías implementar la lógica para el porcentaje crítico.
                    }
                    if (arrowEffectInstantDamage && arrowTier > 0) {
                        ItemStack arrow = new ItemStack(Material.ARROW, 1);
                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, arrowTier);
                        pillager.getEquipment().setItemInOffHand(arrow);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
