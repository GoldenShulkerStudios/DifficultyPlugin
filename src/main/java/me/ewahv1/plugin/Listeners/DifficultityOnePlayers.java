package me.ewahv1.plugin.Listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class DifficultityOnePlayers implements Listener {
    private Random random = new Random();
    private int failProbability = 90; // Puedes cambiar este valor para ajustar la probabilidad de falla
    private JavaPlugin plugin; // Referencia al plugin

    public DifficultityOnePlayers(JavaPlugin plugin) {
        this.plugin = plugin; // Inicializa la referencia al plugin
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
            Bukkit.broadcastMessage("El jugador " + player.getName() + " ha activado un totem");

            if (random.nextInt(100) < failProbability) {
                // Mata al jugador directamente
                player.setHealth(0);
                
                // Cambia el modo de juego del jugador a espectador
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }, 20);
                
                Bukkit.broadcastMessage("El totem de " + player.getName() + " ha fallado con una probabilidad del " + failProbability + "% de fallar");
            }
            else{
                Bukkit.broadcastMessage("El totem de " + player.getName() + " se ha activado con una probabilidad del " + failProbability + "% de fallar");
            }
        }
    }
}
