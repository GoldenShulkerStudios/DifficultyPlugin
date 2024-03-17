package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GhastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("GhastSetExplosionPower")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el poder de explosión del Ghast. Uso correcto: /GhastSetExplosionPower <power>");
                    return true;
                }
                int explosionPower = Integer.parseInt(args[0]);
                if (explosionPower == 0) {
                    sender.sendMessage("El poder de explosión del Ghast no se aplica si el valor es 0");
                    return true;
                }
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE ghastsettings SET ExplosionPower = ? WHERE ID = 1");
                ps.setInt(1, explosionPower);
                ps.executeUpdate();
                sender.sendMessage("El poder de explosión del Ghast se ha establecido en " + explosionPower);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el poder de explosión del Ghast. Uso correcto: /GhastSetExplosionPower <power>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("GhastStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT ExplosionPower FROM ghastsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int explosionPower = rs.getInt("ExplosionPower");
                    sender.sendMessage("El poder de explosión del Ghast está en " + explosionPower);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Ghast. Uso correcto: /GhastStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
