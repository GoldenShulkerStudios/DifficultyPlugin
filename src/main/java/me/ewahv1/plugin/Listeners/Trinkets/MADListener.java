package me.ewahv1.plugin.Listeners.Trinkets;

import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Material;
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
    public void onPhantomDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getEntity();
            if (phantom.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 90) { // Chance de ser dropeado el trinket
                    ItemStack defectiveGravityBackpack;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 50) { // Chance de ser dorado
                        defectiveGravityBackpack = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§6§lMochila Antigravedad Defectuosa Dorada");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 5 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(4);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        defectiveGravityBackpack.setItemMeta(meta);
                    } else {
                        defectiveGravityBackpack = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§6§lMochila Antigravedad Defectuosa");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 3 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(3);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        defectiveGravityBackpack.setItemMeta(meta);
                    }
                    event.getDrops().add(defectiveGravityBackpack);

                    // Actualizar el contador en la base de datos
                    try {
                        java.sql.Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_MAD_Settings SET Counter = Counter + 1 WHERE ID = 1");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
            // Si el tiempo de enfriamiento aún no ha terminado, no hacemos nada
            return;
        }

        if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§6§lMochila Antigravedad Defectuosa")) {
            Random rand = new Random();
            int chance = rand.nextInt(5);
            if (chance == 0) { // Chance de activar el efecto
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 0, true, false, true)); // Slow Falling I durante 3 segundos
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
            }
        } else if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§6§lMochila Antigravedad Defectuosa Dorada")) {
            Random rand = new Random();
            int chance = rand.nextInt(5);
            if (chance == 0) { // Chance de activar el efecto
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, true, false, true)); // Slow Falling I durante 5 segundos
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
            }
        }
    }
}
