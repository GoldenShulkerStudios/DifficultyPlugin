package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Pillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class PillagerListener implements Listener {

    private final DatabaseConnection connection;

    public PillagerListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Pillager) {
            Pillager pillager = (Pillager) event.getEntity();
            loadPillagerSettingsAsync(pillager);
        }
    }

    private void loadPillagerSettingsAsync(Pillager pillager) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT DoubleDamage, CriticPercentage, ArrowEffectInstantDamage, ArrowTier FROM diff_pillager_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getBoolean("DoubleDamage"),
                            rs.getInt("CriticPercentage"),
                            rs.getBoolean("ArrowEffectInstantDamage"),
                            rs.getInt("ArrowTier")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{false, 0, false, 0};
        }).thenAccept(settings -> {
            boolean doubleDamage = (boolean) settings[0];
            int criticPercentage = (int) settings[1];
            boolean arrowEffectInstantDamage = (boolean) settings[2];
            int arrowTier = (int) settings[3];

            if (doubleDamage) {
                pillager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
            }

            if (criticPercentage > 0) {
                // Implementar lógica para el porcentaje crítico
            }

            if (arrowEffectInstantDamage && arrowTier > 0) {
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, arrowTier);
                pillager.getEquipment().setItemInOffHand(arrow);
            }
        });
    }
}
