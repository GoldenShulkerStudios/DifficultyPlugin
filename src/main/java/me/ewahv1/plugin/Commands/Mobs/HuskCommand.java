package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class HuskCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sethuskaxe")) {
            try {
                int axe = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE husksettings SET Axe = ? WHERE ID = 1");
                ps.setInt(1, axe);
                ps.executeUpdate();
                sender.sendMessage("El tipo de hacha del Husk se ha establecido en " + axe);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el tipo de hacha del Husk");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("sethusksharpness")) {
            try {
                int sharpness = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE husksettings SET Sharpness = ? WHERE ID = 1");
                ps.setInt(1, sharpness);
                ps.executeUpdate();
                sender.sendMessage("La nitidez del hacha del Husk se ha establecido en " + sharpness);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la nitidez del hacha del Husk");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}