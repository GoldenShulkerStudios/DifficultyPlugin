package me.ewahv1.plugin.Commands.Difficulty.Totem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;

public class TotemStatusCommand implements CommandExecutor {
    private FailTotemListener failTotemListener;
    private JavaPlugin plugin;
    private DatabaseConnection connection;

    public TotemStatusCommand(FailTotemListener failTotemListener, JavaPlugin plugin, DatabaseConnection connection) {
        this.failTotemListener = failTotemListener;
        this.plugin = plugin;
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            loadSettingsFromDatabaseAsync(player).exceptionally(ex -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> 
                    player.sendMessage("Hubo un error al cargar la configuración de la base de datos.")
                );
                ex.printStackTrace();
                return null;
            });
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }

    private CompletableFuture<Void> loadSettingsFromDatabaseAsync(Player player) {
        return connection.getConnectionAsync().thenAccept(conn -> {
            try (Connection connection = conn; 
                 Statement statement = connection.createStatement(); 
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM totemsettings WHERE ID = 1")) {

                if (resultSet.next()) {
                    boolean isTotemActive = resultSet.getBoolean("Status");
                    int failProbability = resultSet.getInt("FailPorcentage");
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        player.sendMessage("La mecánica de failtotem actualmente está " + (isTotemActive ? "activada" : "desactivada") + ".");
                        player.sendMessage("El porcentaje de falla del totem está establecido en " + failProbability + "%.");
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
