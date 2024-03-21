package me.ewahv1.plugin.Listeners.Trinkets;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MADListener implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPhantomDeath(EntityDeathEvent event) throws SQLException {
        if (event.getEntity() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getEntity();
            if (phantom.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 3) {// Chance de ser dropeado el trinket
                    ItemStack defectiveGravityBackpack;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 1) { // Chance de ser dorado
                        defectiveGravityBackpack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§6§lMochila Antigravedad Defectuosa Dorada");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 5 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(6);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        defectiveGravityBackpack.setItemMeta(meta);

                        // Actualizar el contador dorado en la base de datos
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountGold = CountGold + 1 WHERE ID = 2");
                    } else {
                        defectiveGravityBackpack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§a§lMochila Antigravedad Defectuosa");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 3 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(5);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        defectiveGravityBackpack.setItemMeta(meta);

                        // Actualizar el contador normal en la base de datos
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountNormal = CountNormal + 1 WHERE ID = 2");
                    }
                    event.getDrops().add(defectiveGravityBackpack);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        Long cooldownTime = cooldowns.get(player.getUniqueId());

        if (cooldownTime != null && cooldownTime > System.currentTimeMillis()) {
            return;
        }
        if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§a§lMochila Antigravedad Defectuosa")) {
            Random rand = new Random();
            int chance = rand.nextInt(5);
            if (chance == 0) { // Chance de activar el efecto
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 0, true, false, true)); // Slow Falling I durante 3 segundos
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
            }
        } else if (offHandItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK && offHandItem.getItemMeta().getDisplayName().equals("§6§lMochila Antigravedad Defectuosa Dorada")) {
            Random rand = new Random();
            int chance = rand.nextInt(5);
            if (chance == 0) { // Chance de activar el efecto
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, true, false, true)); // Slow Falling I durante 5 segundos
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
            }
        }
    }
}