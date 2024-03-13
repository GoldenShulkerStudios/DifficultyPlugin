package me.ewahv1.plugin.Listeners.Storm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;

public class StormListener implements Listener {

    private JavaPlugin plugin;
    private int stormTime = 0;
    private int baseStormTime = 120;
    private boolean stormActive = true;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private Objective objective;
    private BossBar bossBar = Bukkit.createBossBar(ChatColor.GRAY + "Tiempo restante de la tormenta: ", BarColor.WHITE, BarStyle.SOLID);
    private BukkitTask stormTask;

    public StormListener(JavaPlugin plugin) {
        this.plugin = plugin;
        if (scoreboard.getObjective("stormTime") == null) {
            objective = scoreboard.registerNewObjective("stormTime", "dummy", ChatColor.GRAY + "Quedan ");
        } else {
            objective = scoreboard.getObjective("stormTime");
        }
    }

    public void setStormTime(int stormTime) {
        this.stormTime = stormTime;
    }

    public void setBaseStormTime(int baseStormTime) {
        this.baseStormTime = baseStormTime;
    }

    public int getStormTime() {
        return this.stormTime;
    }

    public void setStormActive(boolean stormActive) {
        this.stormActive = stormActive;
    }

    public void hideBossBar() {
        this.bossBar.setVisible(false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (stormActive) { // Solo activa la tormenta si stormActive es verdadero
            stormTime += baseStormTime;
            baseStormTime *= 2;
            player.getWorld().setStorm(true);
            bossBar.setVisible(true);
            player.getWorld().setWeatherDuration(stormTime * 20);
            Score score = objective.getScore(ChatColor.WHITE + " segundos de tormenta");
            score.setScore(stormTime);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(onlinePlayer);
            }
            updateBossBar();
            if (stormTask != null) {
                stormTask.cancel();
            }
            stormTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    if (player.getWorld().hasStorm()) {
                        stormTime--;
                        updateBossBar();
                        if (stormTime <= 0) {
                            bossBar.removeAll();
                        }
                    }
                }
            }, 0L, 20L);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().hasStorm()) {
            bossBar.addPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().hasStorm()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "No puedes dormir durante una tormenta!");
        }
    }

    private void updateBossBar() {
        int minutes = stormTime / 60;
        int seconds = stormTime % 60;
        bossBar.setTitle(ChatColor.GRAY + "Tiempo restante de la tormenta: " + minutes + "m " + seconds + "s");
    }
}
