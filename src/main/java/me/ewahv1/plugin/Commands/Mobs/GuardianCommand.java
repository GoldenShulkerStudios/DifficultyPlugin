package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class GuardianCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setguardianresistance")) {
            try {
                int resistance = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE guardiansettings SET Resistance = ? WHERE ID = 1");
                ps.setInt(1, resistance);
                ps.executeUpdate();
                sender.sendMessage("La resistencia del Guardian se ha establecido en " + resistance);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la resistencia del Guardian");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
