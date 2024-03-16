package me.ewahv1.plugin.Commands.Storm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ewahv1.plugin.Listeners.Storm.StormListener;
import me.ewahv1.plugin.Database.Connection;
import java.sql.PreparedStatement;

public class ResetStormCommand implements CommandExecutor {

    private StormListener stormListener;

    public ResetStormCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                java.sql.Connection connection = Connection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE stormsettings SET StormTime = ?, BaseStormTime = ? WHERE ID = 1");

                preparedStatement.setInt(1, 0);
                preparedStatement.setInt(2, 600);

                preparedStatement.executeUpdate();

                stormListener.setStormTime(0);
                stormListener.setBaseStormTime(600);
                stormListener.hideBossBar();
                player.getWorld().setStorm(false);
                player.sendMessage("La tormenta ha sido reiniciada.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
