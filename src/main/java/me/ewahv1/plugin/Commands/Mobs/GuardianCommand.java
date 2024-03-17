package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GuardianCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("GuardianSetResistance")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer la resistencia del Guardian. Uso correcto: /GuardianSetResistance <resistance>");
                    return true;
                }
                int resistance = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE guardiansettings SET Resistance = ? WHERE ID = 1");
                ps.setInt(1, resistance);
                ps.executeUpdate();
                sender.sendMessage("La resistencia del Guardian se ha establecido en " + resistance);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la resistencia del Guardian. Uso correcto: /GuardianSetResistance <resistance>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("GuardianStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Resistance FROM guardiansettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int resistance = rs.getInt("Resistance");
                    sender.sendMessage("La resistencia del Guardian está en " + resistance);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Guardian. Uso correcto: /GuardianStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
