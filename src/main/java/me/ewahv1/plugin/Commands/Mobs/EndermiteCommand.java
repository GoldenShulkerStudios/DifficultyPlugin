package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EndermiteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("EndermiteToggle")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability FROM endermitesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean currentInvulnerability = rs.getBoolean("Invulnerability");
                    boolean newInvulnerability = !currentInvulnerability;
                    ps = Connection.getConnection().prepareStatement("UPDATE endermitesettings SET Invulnerability = ? WHERE ID = 1");
                    ps.setBoolean(1, newInvulnerability);
                    ps.executeUpdate();
                    sender.sendMessage("La invulnerabilidad del Endermite se ha establecido en " + newInvulnerability);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al cambiar la invulnerabilidad del Endermite. Uso correcto: /EndermiteToggle");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("EndermiteSetStrength")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Uso: /EndermiteSetStrength <fuerza>");
                    return true;
                }
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermitesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del Endermite se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del Endermite. Uso correcto: /EndermiteSetStrength <fuerza>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("EndermiteStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability, Strength FROM endermitesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Invulnerability");
                    int strength = rs.getInt("Strength");
                    sender.sendMessage("La invulnerabilidad del Endermite está actualmente en " + invulnerability + " y la fuerza del Endermite está en " + strength);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Endermite. Uso correcto: /EndermiteStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
