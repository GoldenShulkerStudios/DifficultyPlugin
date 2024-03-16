package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class EndermiteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setendermiteinvulnerability")) {
            try {
                boolean invulnerability = Boolean.parseBoolean(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermitesettings SET Invulnerability = ? WHERE ID = 1");
                ps.setBoolean(1, invulnerability);
                ps.executeUpdate();
                sender.sendMessage("La invulnerabilidad del Endermite se ha establecido en " + invulnerability);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la invulnerabilidad del Endermite");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setendermitestrength")) {
            try {
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermitesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del Endermite se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del Endermite");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}