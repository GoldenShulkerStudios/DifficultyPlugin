package me.ewahv1.plugin.Commands.Totem;

import me.ewahv1.plugin.Listeners.Items.FailTotemListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.Statement;

import me.ewahv1.plugin.Database.Connection;

public class TotemStatusCommand implements CommandExecutor {
    private FailTotemListener failTotemListener;

    public TotemStatusCommand(FailTotemListener failTotemListener) {
        this.failTotemListener = failTotemListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            loadSettingsFromDatabase(player);
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }

    private void loadSettingsFromDatabase(Player player) {
        try {
            Statement statement = Connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM totemsettings WHERE ID = 1");

            if (resultSet.next()) {
                boolean isTotemActive = resultSet.getBoolean("Status");
                int failProbability = resultSet.getInt("FailPorcentage");
                player.sendMessage("La mecánica de failtotem actualmente está " + (isTotemActive ? "activada" : "desactivada") + ".");
                player.sendMessage("El porcentaje de falla del totem está establecido en " + failProbability + "%.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
