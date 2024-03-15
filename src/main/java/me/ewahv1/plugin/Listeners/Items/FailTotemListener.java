package me.ewahv1.plugin.Listeners.Items;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import me.ewahv1.plugin.Database.Connection;

public class FailTotemListener implements Listener {
    private Random random = new Random();
    private int failProbability;
    private JavaPlugin plugin;
    private boolean isTotemActive;

    public FailTotemListener(JavaPlugin plugin) {
        this.plugin = plugin;
        loadSettingsFromDatabase();
    }

    private void loadSettingsFromDatabase() {
        try {
            Statement statement = Connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM totemsettings WHERE ID = 1");

            if (resultSet.next()) {
                this.failProbability = resultSet.getInt("FailPorcentage");
                this.isTotemActive = resultSet.getBoolean("Status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFailProbability(int failProbability) {
        this.failProbability = failProbability;
    }

    public int getFailProbability() {
        return this.failProbability;
    }

    public void setTotemActive(boolean isTotemActive) {
        this.isTotemActive = isTotemActive;
    }

    public boolean isTotemActive() {
        return this.isTotemActive;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (player.getHealth() - event.getFinalDamage() > 0) {
            return;
        }

        if ((player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) && isTotemActive) {

            if (random.nextInt(100) < failProbability) {
                // Quita el totem de la mano principal o secundaria del jugador
                if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                } else if (player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                }
                
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "El totem de " + player.getName() + " ha fallado con una probabilidad del " + failProbability + "% de fallar");
            }
        }
    }
}
