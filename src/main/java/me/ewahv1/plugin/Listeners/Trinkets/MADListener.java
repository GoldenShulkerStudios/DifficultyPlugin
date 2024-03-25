package me.ewahv1.plugin.Listeners.Trinkets;
import me.ewahv1.plugin.Database.DatabaseConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                if (chance < 90) {
                    ItemStack defectiveGravityBackpack;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 1) {
                        defectiveGravityBackpack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§6§lMochila Antigravedad Defectuosa Dorada");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 5 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: B.O.T"));
                        meta.setCustomModelData(6);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        defectiveGravityBackpack.setItemMeta(meta);

                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountGold = CountGold + 1 WHERE ID = 2");
                    } else {
                        defectiveGravityBackpack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = defectiveGravityBackpack.getItemMeta();
                        meta.setDisplayName("§a§lMochila Antigravedad Defectuosa");
                        meta.setLore(Arrays.asList("§aEl portador tendrá Slow Falling I durante 3 segundos de manera aleatoria cada 1 segundo", "§6§lSlot: B.O.T"));
                        meta.setCustomModelData(5);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        defectiveGravityBackpack.setItemMeta(meta);

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
        UUID playerUuid = player.getUniqueId();
        Long cooldownTime = cooldowns.get(player.getUniqueId());
    
        if (cooldownTime != null && cooldownTime > System.currentTimeMillis()) {
            return;
        }
    
        // Obtén el inventario de la Bolsa de Trinkets del jugador
        Inventory bag = getBag(playerUuid);
    
        // Verifica si la bolsa es nula antes de intentar acceder a sus contenidos
        if (bag == null) {
            return;
        }
    
        // Verifica cada objeto en la Bolsa de Trinkets
        for (ItemStack item : bag.getContents()) {
            if (item != null && item.getType() == Material.WARPED_FUNGUS_ON_A_STICK) {
                if (item.getItemMeta().getDisplayName().equals("§a§lMochila Antigravedad Defectuosa")) {
                    Random rand = new Random();
                    int chance = rand.nextInt(5);
                    if (chance == 0) { // Chance de activar el efecto
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 0, true, false, true)); // Slow Falling I durante 3 segundos
                        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
                    }
                    return;
                } else if (item.getItemMeta().getDisplayName().equals("§6§lMochila Antigravedad Defectuosa Dorada")) {
                    Random rand = new Random();
                    int chance = rand.nextInt(5);
                    if (chance == 0) { // Chance de activar el efecto
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, true, false, true)); // Slow Falling I durante 5 segundos
                        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 10000); // Añadimos un tiempo de enfriamiento de 10 segundos
                    }
                    return;
                }
            }
        }
    }

    private Inventory getBag(UUID playerUuid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT inventory FROM player_inventories WHERE player_uuid = ?");
            ps.setString(1, playerUuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String inventoryString = rs.getString("inventory");
                return deserializeInventory(inventoryString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Inventory deserializeInventory(String inventoryString) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(inventoryString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar el inventario.", e);
        }
    }
}