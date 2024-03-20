package me.ewahv1.plugin.Commands.Difficulty.Storm;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Difficulty.Storm.StormListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;

public class SetStormTimeCommand implements CommandExecutor {

    private StormListener stormListener;

    public SetStormTimeCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                try {
                    int stormTime = Integer.parseInt(args[0]);
                    stormListener.setStormTime(stormTime);

                    java.sql.Connection connection = DatabaseConnection.getConnection();

                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE stormsettings SET StormTime = ? WHERE ID = 1");

                    preparedStatement.setInt(1, stormTime);

                    preparedStatement.executeUpdate();

                    player.sendMessage("El tiempo de la tormenta se ha establecido en " + stormTime + " segundos.");
                } catch (NumberFormatException e) {
                    player.sendMessage("Por favor, introduce un número válido.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage("Uso: /setstormtime <segundos>");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
