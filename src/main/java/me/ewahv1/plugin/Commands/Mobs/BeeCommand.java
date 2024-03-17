package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("BeeToggle")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Hostility FROM beesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean currentHostility = rs.getBoolean("Hostility");
                    boolean newHostility = !currentHostility;
                    ps = Connection.getConnection().prepareStatement("UPDATE beesettings SET Hostility = ? WHERE ID = 1");
                    ps.setBoolean(1, newHostility);
                    ps.executeUpdate();
                    sender.sendMessage("La hostilidad de la abeja se ha establecido en " + newHostility);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al cambiar la hostilidad de la abeja. Uso correcto: /BeeToggle");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("BeeSetStrength")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Uso: /BeeSetStrength <fuerza>");
                    return true;
                }
                int strength = Integer.parseInt(args[0]);
                if (strength < 0) {
                    sender.sendMessage("La fuerza no puede ser negativa");
                    return true;
                }
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE beesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza de la abeja se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza de la abeja. Uso correcto: /BeeSetStrength <fuerza>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("BeeStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Hostility, Strength FROM beesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean hostility = rs.getBoolean("Hostility");
                    int strength = rs.getInt("Strength");
                    sender.sendMessage("La hostilidad de la abeja está en " + hostility + " y la fuerza de la abeja está en " + strength);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado de la abeja. Uso correcto: /BeeStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
