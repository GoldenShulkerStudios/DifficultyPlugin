package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HuskCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("HuskSetAxe")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el tipo de hacha del Husk. Uso correcto: /HuskSetAxe <tipo>");
                    return true;
                }
                int axe = Integer.parseInt(args[0]);
                String axeType = "";
                switch (axe) {
                    case 0:
                        axeType = "ninguno";
                        break;
                    case 1:
                        axeType = "madera";
                        break;
                    case 2:
                        axeType = "piedra";
                        break;
                    case 3:
                        axeType = "hierro";
                        break;
                    case 4:
                        axeType = "diamante";
                        break;
                    case 5:
                        axeType = "netherite";
                        break;
                }
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE husksettings SET Axe = ? WHERE ID = 1");
                ps.setInt(1, axe);
                ps.executeUpdate();
                sender.sendMessage("El tipo de hacha del Husk se ha establecido en: " + axeType);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el tipo de hacha del Husk. Uso correcto: /HuskSetAxe <tipo>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("HuskSetSharpness")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer El sharpness del hacha del Husk. Uso correcto: /HuskSetSharpness <sharpness>");
                    return true;
                }
                int sharpness = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE husksettings SET Sharpness = ? WHERE ID = 1");
                ps.setInt(1, sharpness);
                ps.executeUpdate();
                sender.sendMessage("El sharpness del hacha del Husk se ha establecido en " + sharpness);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el sharpness del hacha del Husk. Uso correcto: /HuskSetSharpness <sharpness>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("HuskStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Axe, Sharpness FROM husksettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int axe = rs.getInt("Axe");
                    int sharpness = rs.getInt("Sharpness");
                    String axeType = "";
                    switch (axe) {
                        case 0:
                            axeType = "ninguno";
                            break;
                        case 1:
                            axeType = "madera";
                            break;
                        case 2:
                            axeType = "piedra";
                            break;
                        case 3:
                            axeType = "hierro";
                            break;
                        case 4:
                            axeType = "diamante";
                            break;
                        case 5:
                            axeType = "netherite";
                            break;
                    }
                    sender.sendMessage("El tipo de hacha del Husk está en: " + axeType + " y el sharpness del hacha del Husk está en " + sharpness);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Husk. Uso correcto: /HuskStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
