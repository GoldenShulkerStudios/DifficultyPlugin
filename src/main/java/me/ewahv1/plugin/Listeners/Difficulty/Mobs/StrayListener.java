package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Stray;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class StrayListener implements Listener {

    private final DatabaseConnection connection;

    public StrayListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.STRAY) {
            Stray stray = (Stray) event.getEntity();
            loadStraySettingsAsync(stray);
        }
    }

    private void loadStraySettingsAsync(Stray stray) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT BowPower, ArrowEffectSlowness FROM diff_stray_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("BowPower"),
                            rs.getInt("ArrowEffectSlowness")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{0, 0};
        }).thenAccept(settings -> {
            int bowPower = (int) settings[0];
            int arrowEffectSlowness = (int) settings[1];

            if (bowPower > 0) {
                ItemStack bow = new ItemStack(Material.BOW);
                bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, bowPower);
                stray.getEquipment().setItemInMainHand(bow);
            }

            if (arrowEffectSlowness > 0) {
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                PotionMeta meta = (PotionMeta) arrow.getItemMeta();
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, arrowEffectSlowness - 1), true);
                arrow.setItemMeta(meta);
                stray.getEquipment().setItemInOffHand(arrow);
            }
        });
    }
}
