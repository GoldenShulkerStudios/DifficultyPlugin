package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IronGolemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("IronGolemToggle")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT FireResistance FROM irongolemsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int fireResistance = rs.getInt("FireResistance");
                    PreparedStatement psUpdate = Connection.getConnection().prepareStatement("UPDATE irongolemsettings SET FireResistance = ? WHERE ID = 1");
                    psUpdate.setInt(1, fireResistance == 0 ? 1 : 0);
                    psUpdate.executeUpdate();
                    sender.sendMessage("Resistencia al fuego de Iron Golem establecida a " + (fireResistance == 0 ? "Activado" : "Desactivado"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("IronGolemStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT FireResistance FROM irongolemsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int fireResistance = rs.getInt("FireResistance");
                    sender.sendMessage("Estado de resistencia al fuego de Iron Golem: " + (fireResistance == 1 ? "Activado" : "Desactivado"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
