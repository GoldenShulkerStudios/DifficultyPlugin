package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class MagmaCubeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setmagmacubeknockback")) {
            try {
                int knockback = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE magmacubesettings SET Knockback = ? WHERE ID = 1");
                ps.setInt(1, knockback);
                ps.executeUpdate();
                sender.sendMessage("El empuje hacia atrás del MagmaCube se ha establecido en " + knockback);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el empuje hacia atrás del MagmaCube");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setmagmacubestrength")) {
            try {
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE magmacubesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del MagmaCube se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del MagmaCube");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
