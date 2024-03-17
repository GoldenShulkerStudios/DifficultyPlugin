package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HoglinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("HoglinSetKnockback")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el empuje hacia atrás del Hoglin. Uso correcto: /HoglinSetKnockback <knockback>");
                    return true;
                }
                int knockback = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE hoglinsettings SET Knockback = ? WHERE ID = 1");
                ps.setInt(1, knockback);
                ps.executeUpdate();
                sender.sendMessage("El empuje hacia atrás del Hoglin se ha establecido en " + knockback);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el empuje hacia atrás del Hoglin. Uso correcto: /HoglinSetKnockback <knockback>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("HoglinStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Knockback FROM hoglinsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int knockback = rs.getInt("Knockback");
                    sender.sendMessage("El empuje hacia atrás del Hoglin está en " + knockback);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Hoglin. Uso correcto: /HoglinStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
