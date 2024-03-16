package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class HoglinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sethoglinknockback")) {
            try {
                int knockback = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE hoglinsettings SET Knockback = ? WHERE ID = 1");
                ps.setInt(1, knockback);
                ps.executeUpdate();
                sender.sendMessage("El empuje hacia atrás del Hoglin se ha establecido en " + knockback);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el empuje hacia atrás del Hoglin");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
