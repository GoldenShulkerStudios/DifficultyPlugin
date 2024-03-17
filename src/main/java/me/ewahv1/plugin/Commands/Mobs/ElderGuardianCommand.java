package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ElderGuardianCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ElderGuardianSetResistance")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Uso: /ElderGuardianSetResistance <resistencia>");
                    return true;
                }
                int resistance = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE elderguardiansettings SET Resistance = ? WHERE ID = 1");
                ps.setInt(1, resistance);
                ps.executeUpdate();
                sender.sendMessage("La resistencia del Elder Guardian se ha establecido en " + resistance);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la resistencia del Elder Guardian. Uso correcto: /ElderGuardianSetResistance <resistencia>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("ElderGuardianStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Resistance FROM elderguardiansettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int resistance = rs.getInt("Resistance");
                    sender.sendMessage("La resistencia del Elder Guardian está actualmente en " + resistance);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado de resistencia del Elder Guardian. Uso correcto: /ElderGuardianStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
