package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class GhastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setghastexplosionpower")) {
            try {
                int explosionPower = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE ghastsettings SET ExplosionPower = ? WHERE ID = 1");
                ps.setInt(1, explosionPower);
                ps.executeUpdate();
                sender.sendMessage("El poder de explosión del Ghast se ha establecido en " + explosionPower);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el poder de explosión del Ghast");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
