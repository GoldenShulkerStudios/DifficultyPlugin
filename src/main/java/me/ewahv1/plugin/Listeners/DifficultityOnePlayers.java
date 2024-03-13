package me.ewahv1.plugin.Listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class DifficultityOnePlayers implements Listener {
    private Random random = new Random();
    private int failProbability = 10; // Puedes cambiar este valor para ajustar la probabilidad de falla
    private JavaPlugin plugin;

    public DifficultityOnePlayers(JavaPlugin plugin) {
        this.plugin = plugin;
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

        if (player.getInventory().contains(Material.TOTEM_OF_UNDYING)) {

            if (random.nextInt(100) < failProbability) {
                player.setHealth(0);
                
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }, 20);
                
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "El totem de " + player.getName() + " ha fallado con una probabilidad del " + failProbability + "% de fallar");
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.teleport(event.getRespawnLocation());
    }
}
