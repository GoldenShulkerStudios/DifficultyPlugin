package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class DrownedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setdrownedtrident")) {
            try {
                boolean trident = Boolean.parseBoolean(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE drownedsettings SET Trident = ? WHERE ID = 1");
                ps.setBoolean(1, trident);
                ps.executeUpdate();
                sender.sendMessage("Drowned trident set to " + trident);
            } catch (Exception e) {
                sender.sendMessage("Error setting Drowned trident");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setdrownedchanneling")) {
            try {
                int channeling = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE drownedsettings SET Channeling = ? WHERE ID = 1");
                ps.setInt(1, channeling);
                ps.executeUpdate();
                sender.sendMessage("Drowned channeling set to " + channeling);
            } catch (Exception e) {
                sender.sendMessage("Error setting Drowned channeling");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}