package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DrownedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("DrownedToggleTrident")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Trident FROM drownedsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean currentTrident = rs.getBoolean("Trident");
                    boolean newTrident = !currentTrident;
                    ps = Connection.getConnection().prepareStatement("UPDATE drownedsettings SET Trident = ? WHERE ID = 1");
                    ps.setBoolean(1, newTrident);
                    ps.executeUpdate();
                    sender.sendMessage("El tridente del Drowned se ha establecido en " + newTrident);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al cambiar el tridente del Drowned. Uso correcto: /DrownedToggleTrident");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("DrownedStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Trident FROM drownedsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean trident = rs.getBoolean("Trident");
                    sender.sendMessage("El tridente del Drowned está " + (trident ? "habilitado" : "deshabilitado"));
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del tridente del Drowned. Uso correcto: /DrownedStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
