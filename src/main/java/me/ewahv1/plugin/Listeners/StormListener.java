package me.ewahv1.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    private int stormTime = 0; // Variable que almacena la cantidad de tormenta en segundos
    private int baseStormTime = 120; // Tiempo base de la tormenta en segundos (2 minutos)
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective = scoreboard.registerNewObjective("stormTime", "dummy", ChatColor.GRAY + "Quedan ");
    private BossBar bossBar = Bukkit.createBossBar(ChatColor.GRAY + "Tiempo restante de la tormenta: ", BarColor.WHITE, BarStyle.SOLID);
    private BukkitTask stormTask;

    public StormListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setStormTime(int stormTime) {
        this.stormTime = stormTime;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Aumentar el tiempo de tormenta en el tiempo base cada vez que un jugador muere
        stormTime += baseStormTime;
        baseStormTime *= 2; // Duplicar el tiempo base de la tormenta
        player.getWorld().setStorm(true);
        player.getWorld().setWeatherDuration(stormTime * 20); // Convertir segundos a ticks (20 ticks = 1 segundo)

        // Actualizar el marcador con el tiempo restante de la tormenta
        Score score = objective.getScore(ChatColor.WHITE + " segundos de tormenta");
        score.setScore(stormTime);

        // Mostrar un action bar con el tiempo restante de la tormenta
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setScoreboard(scoreboard);
            bossBar.addPlayer(onlinePlayer);
        }

        // Actualizar el action bar con el tiempo restante de la tormenta
        updateBossBar();

        // Cancelar la tarea anterior si existe
        if (stormTask != null) {
            stormTask.cancel();
        }

        // Crear una tarea que se ejecuta cada segundo (20 ticks = 1 segundo)
        stormTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (player.getWorld().hasStorm()) {
                    stormTime--;
                    updateBossBar();
                    if (stormTime <= 0) {
                        bossBar.removeAll(); // Remover todos los jugadores de la barra de jefe cuando el tiempo de la tormenta llegue a 0
                    }
                }
            }
        }, 0L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().hasStorm()) {
            bossBar.addPlayer(player);
        }
    }

    private void updateBossBar() {
        int minutes = stormTime / 60;
        int seconds = stormTime % 60;
        bossBar.setTitle(ChatColor.GRAY + "Tiempo restante de la tormenta: " + minutes + "m " + seconds + "s");
    }
}
