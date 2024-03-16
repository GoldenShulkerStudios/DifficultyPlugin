package me.ewahv1.plugin.Commands.Storm;

import me.ewahv1.plugin.Database.Connection;
import me.ewahv1.plugin.Listeners.Storm.StormListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.Statement;

public class StormStatusCommand implements CommandExecutor {

    private StormListener stormListener;

    public StormStatusCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                java.sql.Connection connection = Connection.getConnection();

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM StormSettings WHERE ID = 1");

                while (resultSet.next()) {
                    int stormTime = resultSet.getInt("StormTime");
                    stormListener.setStormTime(stormTime);
                }

                if (player.getWorld().hasStorm()) {
                    int remainingTime = stormListener.getStormTime();
                    int hours = remainingTime / 3600;
                    int minutes = (remainingTime % 3600) / 60;
                    int seconds = remainingTime % 60;
                    player.sendMessage("Actualmente hay una tormenta en tu mundo. Quedan " + hours + " horas, " + minutes + " minutos y " + seconds + " segundos.");
                } else {
                    player.sendMessage("Actualmente no hay una tormenta en tu mundo.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
