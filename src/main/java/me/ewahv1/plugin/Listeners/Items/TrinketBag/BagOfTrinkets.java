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
    
        Inventory inventory;
        if (!playerExistsInDatabase(playerUuid)) {
            // Si el jugador no existe en la base de datos, crea un nuevo inventario y lo guarda en la base de datos.
            inventory = Bukkit.createInventory(null, 9, "Bolsa de Trinkets");
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO player_inventories (player_uuid, inventory) VALUES (?, ?)");
                ps.setString(1, playerUuid.toString());
                ps.setString(2, serializeInventory(inventory));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Si el jugador ya existe en la base de datos, carga su inventario desde la base de datos.
            inventory = loadInventoryFromDatabase(playerUuid);
        }
    
        // Guarda el inventario en el mapa usando el UUID del jugador como clave.
        playerInventories.put(playerUuid, inventory);
    
        // Da al jugador la Bolsa de Trinkets.
        ItemStack trinketBag = new ItemStack(Material.CHEST);
        ItemMeta meta = trinketBag.getItemMeta();
        meta.setDisplayName("Bolsa de Trinkets");
        trinketBag.setItemMeta(meta);
        player.getInventory().addItem(trinketBag);
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
    
        // Obtiene el inventario de la "Bolsa de Trinkets" del jugador.
        Inventory trinketInventory = playerInventories.get(playerUuid);
    
        if (trinketInventory != null) {
            // Guarda el inventario en la base de datos.
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE player_inventories SET inventory = ? WHERE player_uuid = ?");
                ps.setString(1, serializeInventory(trinketInventory));
                ps.setString(2, playerUuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Recorre el inventario del jugador para encontrar la "Bolsa de Trinkets".
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                // Si encuentra la "Bolsa de Trinkets", la elimina del inventario del jugador.
                if (item.getType() == Material.CHEST && "Bolsa de Trinkets".equals(meta.getDisplayName())) {
                    player.getInventory().remove(item);
                    break;
                }
            }
        }
    }
    

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            UUID playerUuid = player.getUniqueId();
    
            // Verifica si el inventario que se está cerrando es la "Bolsa de Trinkets"
            if (event.getView().getTitle().equals("Bolsa de Trinkets")) {
                // Obtén el inventario de la Bolsa de Trinkets del jugador
                Inventory bag = playerInventories.get(playerUuid);
    
                // Verifica si la bolsa es nula antes de intentar serializarla
                if (bag != null) {
                    try {
                        // Actualiza el inventario en la base de datos cuando el jugador cierra la "Bolsa de Trinkets".
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE player_inventories SET inventory = ? WHERE player_uuid = ?");
                        ps.setString(1, serializeInventory(bag));
                        ps.setString(2, playerUuid.toString());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Verifica si el evento es de un jugador y si el inventario abierto es la Bolsa de Trinkets.
        if (event.getWhoClicked() instanceof Player && event.getView().getTitle().equals("Bolsa de Trinkets")) {
            Player player = (Player) event.getWhoClicked(); // Obtiene el jugador.
            ItemStack clickedItem = event.getCurrentItem(); // Obtiene el objeto en el que se hizo clic.
            
            // Verifica si el objeto no es nulo y si es un WARPED_FUNGUS_ON_A_STICK.
            if (clickedItem != null && clickedItem.getType() == Material.WARPED_FUNGUS_ON_A_STICK) {
                // Si es un trinket, permite colocarlo en la Bolsa de Trinkets.
                return;
            } else {
                // Si no es un trinket, cancela el evento y envía un mensaje al jugador.
                event.setCancelled(true);
                player.sendMessage("¡Error! Solo puedes colocar objetos de tipo WARPED_FUNGUS_ON_A_STICK en la Bolsa de Trinkets.");
                return;
            }
        }
    }
    

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
    
        if (item != null && item.hasItemMeta() && "Bolsa de Trinkets".equals(item.getItemMeta().getDisplayName())) {
            // Abre el inventario de la "Bolsa de Trinkets" para el jugador.
            Inventory trinketInventory = playerInventories.get(player.getUniqueId());
            if (trinketInventory != null) {
                player.openInventory(trinketInventory);
            }
            event.setCancelled(true);
        }
    }

    // Este método convierte un inventario en una cadena de texto para poder guardarlo en la base de datos.
    private String serializeInventory(Inventory inventory) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // Crea un flujo de salida de bytes.
        try {
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream); // Crea un flujo de salida de objetos.
            dataOutput.writeInt(inventory.getSize()); // Escribe el tamaño del inventario en el flujo de salida.

            // Escribe cada objeto del inventario en el flujo de salida.
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close(); // Cierra el flujo de salida.
            // Convierte el flujo de salida en una cadena de texto y la devuelve.
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo guardar el inventario.", e);
        }
    }

    // Este método convierte una cadena de texto en un inventario para poder cargarlo desde la base de datos.
    private Inventory deserializeInventory(String inventoryString) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(inventoryString)); // Crea un flujo de entrada de bytes a partir de la cadena de texto.
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream); // Crea un flujo de entrada de objetos a partir del flujo de entrada de bytes.
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt()); // Crea un inventario vacío con el tamaño leído del flujo de entrada.

            // Lee cada objeto del flujo de entrada y lo coloca en el inventario.
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close(); // Cierra el flujo de entrada.
            return inventory; // Devuelve el inventario.
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar el inventario.", e);
        }
    }
}
