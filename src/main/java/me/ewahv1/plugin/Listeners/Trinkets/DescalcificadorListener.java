package me.ewahv1.plugin.Listeners.Trinkets;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

public class DescalcificadorListener implements Listener {

    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent event) throws SQLException {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (skeleton.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 3) {// Chance de ser dropeado el trinket
                    ItemStack warpedFungusStick;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 1) { // Chance de ser dorado
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§6§lDescalcificador dorado");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +2❤ a los Esqueletos", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(4);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        warpedFungusStick.setItemMeta(meta);

                        // Actualizar el contador dorado en la base de datos
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountGold = CountGold + 1 WHERE ID = 1");
                    } else {
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§a§lDescalcificador");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +1❤ a los Esqueletos", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(3);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        warpedFungusStick.setItemMeta(meta);

                        // Actualizar el contador normal en la base de datos
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountNormal = CountNormal + 1 WHERE ID = 1");
                    }
                    event.getDrops().add(warpedFungusStick);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageSkeleton(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Skeleton) {
            Player player = (Player) event.getDamager();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§a§lDescalcificador")) {
                event.setDamage(event.getDamage() + 1);
            } else if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§6§lDescalcificador dorado")) {
                event.setDamage(event.getDamage() + 2);
            }
        }
    }
}
