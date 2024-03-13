package me.ewahv1.plugin.Listeners.Items;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FailTotemListener implements Listener {
    private Random random = new Random();
    private int failProbability = 10; // Puedes cambiar este valor para ajustar la probabilidad de falla
    private JavaPlugin plugin;
    private boolean isTotemActive = true; // Asume que el totem está activo por defecto

    public FailTotemListener(JavaPlugin plugin) {
        this.plugin = plugin;
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
