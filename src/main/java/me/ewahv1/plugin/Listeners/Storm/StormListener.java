package me.ewahv1.plugin.Listeners.Storm;

import me.ewahv1.plugin.Database.Connection;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StormListener implements Listener {

    private JavaPlugin plugin;
    private int stormTime;
    private int baseStormTime;
    private boolean stormActive;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private Objective objective;
    private BossBar bossBar = Bukkit.createBossBar(ChatColor.GRAY + "Tiempo restante de la tormenta: ", BarColor.WHITE, BarStyle.SOLID);
    private BukkitTask stormTask;

    public StormListener(JavaPlugin plugin) {
        this.plugin = plugin;
        loadSettingsFromDatabase();
        if (scoreboard.getObjective("stormTime") == null) {
            objective = scoreboard.registerNewObjective("stormTime", "dummy", ChatColor.GRAY + "Quedan ");
        } else {
            objective = scoreboard.getObjective("stormTime");
        }
    }

    private void loadSettingsFromDatabase() {
        try {
            java.sql.Connection connection = Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stormsettings WHERE ID = 1");
            while (resultSet.next()) {
                stormTime = resultSet.getInt("StormTime");
                baseStormTime = resultSet.getInt("BaseStormTime");
                stormActive = resultSet.getBoolean("StormActive");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStormTime(int stormTime) {
        this.stormTime = stormTime;
    }

    public void setBaseStormTime(int baseStormTime) {
        this.baseStormTime = baseStormTime;
    }

    public void setStormActive(boolean stormActive) {
        this.stormActive = stormActive;
    }

    public int getStormTime() {
        return this.stormTime;
    }

    public void hideBossBar() {
        this.bossBar.setVisible(false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (stormActive) {
            stormTime += baseStormTime;
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
                        try {
                            // Obtiene una conexión a la base de datos
                            java.sql.Connection connection = Connection.getConnection();

                            // Crea un objeto PreparedStatement para enviar consultas SQL a la base de datos
                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE stormsettings SET StormTime = ? WHERE ID = 1");

                            // Establece los valores de las variables en la consulta SQL
                            preparedStatement.setInt(1, stormTime); // Actualiza el tiempo de la tormenta

                            // Ejecuta la consulta SQL
                            preparedStatement.executeUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
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
        int hours = stormTime / 3600;
        int minutes = (stormTime % 3600) / 60;
        int seconds = stormTime % 60;
        bossBar.setTitle(ChatColor.GRAY + "Tiempo restante de la tormenta: " + hours + "h " + minutes + "m " + seconds + "s");
    }
}
