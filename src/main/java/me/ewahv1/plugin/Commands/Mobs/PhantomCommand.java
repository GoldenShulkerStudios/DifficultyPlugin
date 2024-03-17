package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhantomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("PhantomSetSize")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el tamaño del Phantom. Uso correcto: /PhantomSetSize <size>");
                    return true;
                }
                int size = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE phantomsettings SET Size = ? WHERE ID = 1");
                ps.setInt(1, size);
                ps.executeUpdate();
                sender.sendMessage("El tamaño del Phantom se ha establecido en " + size);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el tamaño del Phantom. Uso correcto: /PhantomSetSize <size>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("PhantomStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Size FROM phantomsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int size = rs.getInt("Size");
                    sender.sendMessage("El tamaño del Phantom está en " + size);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del Phantom. Uso correcto: /PhantomStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
