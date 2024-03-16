package me.ewahv1.plugin.Commands.Storm;

import me.ewahv1.plugin.Database.Connection;
import me.ewahv1.plugin.Listeners.Storm.StormListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;

public class ToggleStormCommand implements CommandExecutor {

    private StormListener stormListener;

    public ToggleStormCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                java.sql.Connection connection = Connection.getConnection();


                PreparedStatement preparedStatement;

                if (player.getWorld().hasStorm()) {
                    player.getWorld().setStorm(false);
                    stormListener.setStormActive(false);
                    stormListener.setStormTime(0);
                    stormListener.hideBossBar();
                    player.sendMessage("La tormenta ha sido desactivada.");

                    preparedStatement = connection.prepareStatement("UPDATE StormSettings SET StormActive = false, StormTime = 0 WHERE ID = 1");
                } else {
                    stormListener.setStormActive(true);
                    player.sendMessage("La tormenta ha sido activada. Comenzará cuando un jugador muera.");

                    preparedStatement = connection.prepareStatement("UPDATE StormSettings SET StormActive = true WHERE ID = 1");
                }

                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
