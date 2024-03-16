package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class PiglinBruteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setpiglinbrutespawn")) {
            try {
                int spawn = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE piglinbrutesettings SET Spawn = ? WHERE ID = 1");
                ps.setInt(1, spawn);
                ps.executeUpdate();
                sender.sendMessage("El spawn del PiglinBrute se ha establecido en " + spawn);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el spawn del PiglinBrute");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}