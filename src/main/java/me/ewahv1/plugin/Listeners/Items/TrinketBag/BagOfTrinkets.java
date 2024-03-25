package me.ewahv1.plugin.Listeners.Items.TrinketBag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.ewahv1.plugin.Database.DatabaseConnection;

public class BagOfTrinkets implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<UUID, Inventory> playerInventories = new HashMap<>();

    public BagOfTrinkets(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (!playerExistsInDatabase(playerUuid)) {
            Inventory inventory = Bukkit.createInventory(null, 9, "Bolsa de Trinkets");
            playerInventories.put(playerUuid, inventory);

            ItemStack trinketBag = new ItemStack(Material.CHEST);
            ItemMeta meta = trinketBag.getItemMeta();
            meta.setDisplayName("Bolsa de Trinkets");
            trinketBag.setItemMeta(meta);

            player.getInventory().addItem(trinketBag);

            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO player_inventories (player_uuid, inventory) VALUES (?, ?)");
                ps.setString(1, playerUuid.toString());
                ps.setString(2, serializeInventory(inventory));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Inventory inventory = loadInventoryFromDatabase(playerUuid);
            playerInventories.put(playerUuid, inventory);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE player_inventories SET inventory = ? WHERE player_uuid = ?");
            ps.setString(1, serializeInventory(playerInventories.get(playerUuid)));
            ps.setString(2, playerUuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (event.getView().getTitle().equals("Bolsa de Trinkets")) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE player_inventories SET inventory = ? WHERE player_uuid = ?");
                ps.setString(1, serializeInventory(playerInventories.get(playerUuid)));
                ps.setString(2, playerUuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UUID playerUuid = player.getUniqueId();
    
            if (event.getView().getTitle().equals("Bolsa de Trinkets")) {
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() == Material.WARPED_FUNGUS_ON_A_STICK) {
                    // El objeto es un trinket y puede ser colocado en la Bolsa de Trinkets.
                    return;
                } else if (item != null) {
                    event.setCancelled(true);
                    player.sendMessage("¡Solo puedes poner trinkets en la Bolsa de Trinkets!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.hasItemMeta() && "Bolsa de Trinkets".equals(item.getItemMeta().getDisplayName())) {
            player.openInventory(playerInventories.get(player.getUniqueId()));
            event.setCancelled(true);
        }
    }

    private String serializeInventory(Inventory inventory) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo guardar el inventario.", e);
        }
    }

    private Inventory loadInventoryFromDatabase(UUID playerUuid) {
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

    private boolean playerExistsInDatabase(UUID playerUuid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM player_inventories WHERE player_uuid = ?");
            ps.setString(1, playerUuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isItemInBag(UUID playerUuid, ItemStack item) {
        Inventory inventory = playerInventories.get(playerUuid);
        return inventory != null && inventory.contains(item);
    }
}
