package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Skeleton;
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

public class SkeletonListener implements Listener {

    private final DatabaseConnection connection;

    public SkeletonListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            loadSkeletonSettingsAsync(skeleton);
        }
    }

    private void loadSkeletonSettingsAsync(Skeleton skeleton) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT PowerBow, ArrowEffectInstantDamage FROM diff_skeleton_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("PowerBow"),
                            rs.getInt("ArrowEffectInstantDamage")
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Object[]{0, 0};
        }).thenAccept(settings -> {
            int powerBow = (int) settings[0];
            int arrowEffectInstantDamage = (int) settings[1];

            if (powerBow > 0) {
                ItemStack bow = new ItemStack(Material.BOW, 1);
                bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, powerBow);
                skeleton.getEquipment().setItemInMainHand(bow);
            }

            if (arrowEffectInstantDamage > 0) {
                skeleton.addPotionEffect(new PotionEffect(PotionEffectType.HARM, Integer.MAX_VALUE, arrowEffectInstantDamage - 1, false, false));
            }
        });
    }
}
