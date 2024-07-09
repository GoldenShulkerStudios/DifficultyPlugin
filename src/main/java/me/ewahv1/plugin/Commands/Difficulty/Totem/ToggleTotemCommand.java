package me.ewahv1.plugin.Commands.Difficulty.Totem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;

public class ToggleTotemCommand implements CommandExecutor {
    private final FailTotemListener failTotemListener;
    private final JavaPlugin plugin;
    private final DatabaseConnection connection;

    public ToggleTotemCommand(FailTotemListener failTotemListener, JavaPlugin plugin, DatabaseConnection connection) {
        this.failTotemListener = failTotemListener;
        this.plugin = plugin;
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !args[0].equalsIgnoreCase("totem") || !args[1].equalsIgnoreCase("toggle")) {
            sender.sendMessage("Uso: /difficulty totem toggle");
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean isTotemActive = failTotemListener.isTotemActive();
            failTotemListener.setTotemActive(!isTotemActive);
            updateDatabaseAsync(!isTotemActive).thenRun(() ->
                plugin.getServer().getScheduler().runTask(plugin, () ->
                    player.sendMessage("La mecánica de failtotem ahora está " + (!isTotemActive ? "activada" : "desactivada") + ".")
                )
            ).exceptionally(ex -> {
                plugin.getServer().getScheduler().runTask(plugin, () ->
                    player.sendMessage("Hubo un error al actualizar la base de datos.")
                );
                ex.printStackTrace();
                return null;
            });
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }

    private CompletableFuture<Void> updateDatabaseAsync(boolean status) {
        return connection.getConnectionAsync().thenAccept(conn -> {
            try (Connection connection = conn;
                 PreparedStatement statement = connection.prepareStatement("UPDATE totemsettings SET Status = ? WHERE ID = 1")) {
                statement.setBoolean(1, status);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
