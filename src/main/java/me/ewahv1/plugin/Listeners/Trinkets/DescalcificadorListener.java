package me.ewahv1.plugin.Listeners.Trinkets;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

public class DescalcificadorListener implements Listener {

    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (skeleton.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 90) {// Chance de ser dropeado el trinket
                    ItemStack warpedFungusStick;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 50) { // Chance de ser dorado
                        warpedFungusStick = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§6§lDescalcificador dorado");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +2❤ a los Esqueletos", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(4);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        warpedFungusStick.setItemMeta(meta);
                    } else {
                        warpedFungusStick = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§a§lDescalcificador");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +1❤ a los Esqueletos", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(3);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        warpedFungusStick.setItemMeta(meta);
                    }
                    event.getDrops().add(warpedFungusStick);

                    // Actualizar el contador en la base de datos
                    try {
                        java.sql.Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_Descalcificador_Settings SET Counter = Counter + 1 WHERE ID = 1");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageSkeleton(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && isSkeletonType(event.getEntity())) {
            Player player = (Player) event.getDamager();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§a§lDescalcificador")) {
                event.setDamage(event.getDamage() + 2);
            } else if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§6§lDescalcificador dorado")) {
                event.setDamage(event.getDamage() + 4);
            }
        }
    }

    private boolean isSkeletonType(Entity entity) {
        return entity instanceof Skeleton || entity instanceof Stray || entity instanceof WitherSkeleton;
    }
}
