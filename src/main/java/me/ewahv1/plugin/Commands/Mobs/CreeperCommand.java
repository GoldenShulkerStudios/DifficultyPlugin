package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreeperCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("CreeperSetExplosionSpeed")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Uso: /CreeperSetExplosionSpeed <velocidad>");
                    return true;
                }
                double explosionSpeed = Double.parseDouble(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE creepersettings SET ExplosionSpeed = ? WHERE ID = 1");
                ps.setDouble(1, explosionSpeed);
                ps.executeUpdate();
                sender.sendMessage("La velocidad de explosión del Creeper se ha establecido en " + explosionSpeed);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la velocidad de explosión del Creeper. Uso correcto: /CreeperSetExplosionSpeed <velocidad>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("CreeperStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT ExplosionSpeed FROM creepersettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double explosionSpeed = rs.getDouble("ExplosionSpeed");
                    sender.sendMessage("La velocidad de explosión del Creeper está actualmente en " + explosionSpeed);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado de la velocidad de explosión del Creeper. Uso correcto: /CreeperStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
