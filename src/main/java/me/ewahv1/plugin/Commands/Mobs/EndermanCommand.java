package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EndermanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("EndermanSetSpeed")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer la velocidad del Enderman. Uso correcto: /EndermanSetSpeed <velocidad>");
                    return true;
                }
                int speed = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermansettings SET Speed = ? WHERE ID = 1");
                ps.setInt(1, speed);
                ps.executeUpdate();
                sender.sendMessage("La velocidad del Enderman se ha establecido en " + speed);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la velocidad del Enderman. Uso correcto: /EndermanSetSpeed <velocidad>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("EndermanSetStrength")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer la fuerza del Enderman. Uso correcto: /EndermanSetStrength <fuerza>");
                    return true;
                }
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermansettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del Enderman se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del Enderman. Uso correcto: /EndermanSetStrength <fuerza>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("EndermanStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Speed, Strength FROM endermansettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int speed = rs.getInt("Speed");
                    int strength = rs.getInt("Strength");
                    sender.sendMessage("La velocidad del Enderman está en " + speed + " y la fuerza del Enderman está en " + strength);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Enderman. Uso correcto: /EndermanStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
