package me.ewahv1.plugin.Listeners.Difficulty.Mobs;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.DayListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PigZombie;
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

public class ZombifiedPiglinListener implements Listener {

    private final DatabaseConnection connection;

    public ZombifiedPiglinListener(DatabaseConnection connection) {
        this.connection = connection;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof PigZombie) {
            PigZombie pigZombie = (PigZombie) event.getEntity();
            loadPigZombieSettingsAsync(pigZombie);
        }
    }

    private void loadPigZombieSettingsAsync(PigZombie pigZombie) {
        int currentDay = DayListener.getCurrentDay();

        CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnectionAsync().join();
                 PreparedStatement ps = conn.prepareStatement("SELECT Speed, SwordMaterial, FireAspect FROM diff_zombifiedpiglin_settings WHERE ID = ?")) {

                ps.setInt(1, currentDay);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new int[]{rs.getInt("Speed"), rs.getInt("SwordMaterial"), rs.getInt("FireAspect")};
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new int[]{0, 0, 0};
        }).thenAccept(settings -> {
            int speed = settings[0];
            int swordMaterial = settings[1];
            int fireAspect = settings[2];

            if (speed > 0) {
                pigZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
            }
            if (swordMaterial > 0) {
                Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
                ItemStack sword = new ItemStack(materials[swordMaterial - 1]);
                if (fireAspect > 0) {
                    sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fireAspect);
                }
                pigZombie.getEquipment().setItemInMainHand(sword);
            }
        });
    }
}
