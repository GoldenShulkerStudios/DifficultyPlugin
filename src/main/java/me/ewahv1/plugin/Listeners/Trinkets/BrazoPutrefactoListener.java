package me.ewahv1.plugin.Listeners.Trinkets;

import org.bukkit.Material;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

public class BrazoPutrefactoListener implements Listener {

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.getKiller() instanceof Player) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                if (chance < 90) {// Chance de ser dropeado el trinket
                    ItemStack warpedFungusStick;
                    int goldenChance = rand.nextInt(100);
                    if (goldenChance < 50) { // Chance de ser dorado
                        warpedFungusStick = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§6§lBrazo putrefacto dorado");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +2❤ a los zombies", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(2);
                        warpedFungusStick.setItemMeta(meta);
                    } else {
                        warpedFungusStick = new ItemStack(Material.SHULKER_SHELL, 1);
                        ItemMeta meta = warpedFungusStick.getItemMeta();
                        meta.setDisplayName("§a§lBrazo putrefacto");
                        meta.setLore(Arrays.asList("§aTus ataques básicos realizan +1❤ a los zombies", "§6§lSlot: Mano secundaria"));
                        meta.setCustomModelData(1);
                        warpedFungusStick.setItemMeta(meta);
                    }
                    event.getDrops().add(warpedFungusStick);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageZombie(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && isZombieType(event.getEntity())) {
            Player player = (Player) event.getDamager();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§a§lBrazo putrefacto")) {
                event.setDamage(event.getDamage() + 2);
            } else if (offHandItem.getType() == Material.SHULKER_SHELL && offHandItem.getItemMeta().getDisplayName().equals("§a§lBrazo putrefacto dorado")) {
                event.setDamage(event.getDamage() + 4);
            }
        }
    }

    private boolean isZombieType(Entity entity) {
        return entity instanceof Zombie || entity instanceof Husk || entity instanceof Zoglin || entity instanceof Drowned;
    }
}