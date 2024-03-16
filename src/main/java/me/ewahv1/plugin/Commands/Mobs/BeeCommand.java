package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class BeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setBeeHostility")) {
            try {
                boolean hostility = Boolean.parseBoolean(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE beesettings SET Hostility = ? WHERE ID = 1");
                ps.setBoolean(1, hostility);
                ps.executeUpdate();
                sender.sendMessage("Bee hostility set to " + hostility);
            } catch (Exception e) {
                sender.sendMessage("Error setting Bee hostility");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setBeeStrength")) {
            try {
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE beesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("Bee strength set to " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error setting Bee strength");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
