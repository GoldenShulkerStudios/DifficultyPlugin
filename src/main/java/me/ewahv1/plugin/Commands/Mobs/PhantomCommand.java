package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class PhantomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setphantomsize")) {
            try {
                int size = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE phantomsettings SET Size = ? WHERE ID = 1");
                ps.setInt(1, size);
                ps.executeUpdate();
                sender.sendMessage("El tamaño del Phantom se ha establecido en " + size);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el tamaño del Phantom");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}