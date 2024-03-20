package me.ewahv1.plugin.Listeners.Trinkets;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
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

public class ZombArmListener implements Listener {

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) throws SQLException {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 3) {// Chance de ser dropeado el trinket
                    ItemStack warpedFungusStick;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 10) { // Chance de ser dorado
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§6§lBrazo putrefacto dorado");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +2❤ a los zombies", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(2);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        warpedFungusStick.setItemMeta(meta);
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_zombarm_settings SET CounterGold = CounterGold + 1 WHERE ID = 1");
                    } else {
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§a§lBrazo putrefacto");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +1❤ a los zombies", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(1);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        warpedFungusStick.setItemMeta(meta);
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_zombarm_settings SET CounterNormal = CounterNormal + 1 WHERE ID = 1");
                    }
                    event.getDrops().add(warpedFungusStick);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageZombie(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Zombie) {
            Player player = (Player) event.getDamager();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§a§lBrazo putrefacto")) {
                event.setDamage(event.getDamage() + 1);
            } else if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§6§lBrazo putrefacto dorado")) {
                event.setDamage(event.getDamage() + 2);
            }
        }
    }
}
