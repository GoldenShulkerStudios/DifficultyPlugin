package me.ewahv1.plugin.Listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DifficultityOnePlayers implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            if (new Random().nextInt(10) < 9) { // 90% de probabilidad
                player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                player.damage(10);
                player.sendMessage(ChatColor.RED + "¡Tu tótem de inmortalidad falló en salvarte!");
            }
            else{
                player.sendMessage(ChatColor.RED + "¡Tu tótem de inmortalidad funcionó en salvarte!");
            }
        }
    }
}
