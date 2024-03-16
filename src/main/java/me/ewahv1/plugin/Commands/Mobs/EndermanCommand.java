package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class EndermanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setendermanspeed")) {
            try {
                int speed = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermansettings SET Speed = ? WHERE ID = 1");
                ps.setInt(1, speed);
                ps.executeUpdate();
                sender.sendMessage("La velocidad del Enderman se ha establecido en " + speed);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la velocidad del Enderman");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setendermanstrength")) {
            try {
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE endermansettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del Enderman se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del Enderman");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
