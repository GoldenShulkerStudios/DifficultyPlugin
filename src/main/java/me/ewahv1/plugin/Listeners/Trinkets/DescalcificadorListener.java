package me.ewahv1.plugin.Listeners.Trinkets;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Items.TrinketBag.BagOfTrinkets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class DescalcificadorListener implements Listener {
    private final JavaPlugin plugin;
    private final BagOfTrinkets bagOfTrinkets;

    public DescalcificadorListener(JavaPlugin plugin, BagOfTrinkets bagOfTrinkets) {
        this.plugin = plugin;
        this.bagOfTrinkets = bagOfTrinkets;
    }
    
    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent event) throws SQLException {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (skeleton.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 90) {
                    ItemStack warpedFungusStick;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 1) {
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§6§lDescalcificador dorado");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +2❤ a los Esqueletos", "§6§lSlot: B.O.T", "§6§lTrinket"));
                        meta.setCustomModelData(4);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        warpedFungusStick.setItemMeta(meta);
    
                        Connection connection = DatabaseConnection.getConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("UPDATE tri_count_settings SET CountGold = CountGold + 1 WHERE ID = 1");
                    } else {
                        warpedFungusStick = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§a§lDescalcificador");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +1❤ a los Esqueletos", "§6§lSlot: B.O.T", "§6§lTrinket"));
                        meta.setCustomModelData(3);
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        warpedFungusStick.setItemMeta(meta);
    
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
            UUID playerUuid = player.getUniqueId();

            // Obtén el inventario de la Bolsa de Trinkets del jugador
            Inventory bag = getBag(playerUuid);

            // Verifica cada objeto en la Bolsa de Trinkets
            for (ItemStack item : bag.getContents()) {
                if (item != null && item.getType() == Material.WARPED_FUNGUS_ON_A_STICK) {
                    if (item.getItemMeta().getDisplayName().equals("§a§lDescalcificador")) {
                        event.setDamage(event.getDamage() + 1);
                        return;
                    } else if (item.getItemMeta().getDisplayName().equals("§6§lDescalcificador dorado")) {
                        event.setDamage(event.getDamage() + 2);
                        return;
                    }
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