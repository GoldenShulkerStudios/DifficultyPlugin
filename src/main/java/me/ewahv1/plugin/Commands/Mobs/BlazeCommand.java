package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BlazeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("toggleblaze")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Inmulnerability FROM blazesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean currentInvulnerability = rs.getBoolean("Inmulnerability");
                    boolean newInvulnerability = !currentInvulnerability;
                    ps = Connection.getConnection().prepareStatement("UPDATE blazesettings SET Inmulnerability = ? WHERE ID = 1");
                    ps.setBoolean(1, newInvulnerability);
                    ps.executeUpdate();
                    sender.sendMessage("La invulnerabilidad del Blaze se establecio en " + newInvulnerability);
                }
            } catch (Exception e) {
                sender.sendMessage("Error toggling Blaze invulnerability");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("blazestatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Inmulnerability FROM blazesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Inmulnerability");
                    sender.sendMessage("La invulnerabilidad del Blaze está actualmente en " + invulnerability);
                }
            } catch (Exception e) {
                sender.sendMessage("Error getting Blaze invulnerability status");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
