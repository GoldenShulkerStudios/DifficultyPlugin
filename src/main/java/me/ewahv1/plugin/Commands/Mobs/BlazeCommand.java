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
        if (command.getName().equalsIgnoreCase("BlazeToggle")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability FROM blazesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean currentInvulnerability = rs.getBoolean("Invulnerability");
                    boolean newInvulnerability = !currentInvulnerability;
                    ps = Connection.getConnection().prepareStatement("UPDATE blazesettings SET Invulnerability = ? WHERE ID = 1");
                    ps.setBoolean(1, newInvulnerability);
                    ps.executeUpdate();
                    sender.sendMessage("La invulnerabilidad del Blaze se ha establecido en " + newInvulnerability);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al cambiar la invulnerabilidad del Blaze. Uso correcto: /BlazeToggle");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("BlazeStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Invulnerability FROM blazesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean invulnerability = rs.getBoolean("Invulnerability");
                    sender.sendMessage("La invulnerabilidad del Blaze está actualmente en " + invulnerability);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado de invulnerabilidad del Blaze. Uso correcto: /BlazeStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
