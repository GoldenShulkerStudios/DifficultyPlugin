package me.ewahv1.plugin.Commands.Difficulty.Totem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;

public class SetFailTotemCommand implements CommandExecutor {
    private final FailTotemListener failTotemListener;
    private final JavaPlugin plugin;
    private final DatabaseConnection connection;

    public SetFailTotemCommand(FailTotemListener failTotemListener, JavaPlugin plugin, DatabaseConnection connection) {
        this.failTotemListener = failTotemListener;
        this.plugin = plugin;
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3 || !args[0].equalsIgnoreCase("totem") || !args[1].equalsIgnoreCase("setFail")) {
            sender.sendMessage("Uso: /difficulty totem setFail <porcentaje>");
            return false;
        }

        try {
            int percentage = Integer.parseInt(args[2]);
            if (percentage < 0 || percentage > 100) {
                sender.sendMessage("El porcentaje debe estar entre 0 y 100.");
                return false;
            }

            failTotemListener.setFailProbability(percentage);
            updateDatabaseAsync(percentage).thenRun(() ->
                plugin.getServer().getScheduler().runTask(plugin, () ->
                    sender.sendMessage("El porcentaje de falla del totem ahora está establecido en " + percentage + "%.")
                )
            ).exceptionally(ex -> {
                plugin.getServer().getScheduler().runTask(plugin, () ->
                    sender.sendMessage("Hubo un error al actualizar la base de datos.")
                );
                ex.printStackTrace();
                return null;
            });

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Por favor, proporciona un número válido.");
            return false;
        }
    }

    private CompletableFuture<Void> updateDatabaseAsync(int percentage) {
        return connection.getConnectionAsync().thenAccept(conn -> {
            try (Connection connection = conn;
                 PreparedStatement statement = connection.prepareStatement("UPDATE totemsettings SET FailPorcentage = ? WHERE ID = 1")) {
                statement.setInt(1, percentage);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
