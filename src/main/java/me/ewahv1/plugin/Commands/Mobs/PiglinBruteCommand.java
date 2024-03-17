package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PiglinBruteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("PiglinBruteSetSpawn")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el spawn del PiglinBrute. Uso correcto: /PiglinBruteSetSpawn <spawn>");
                    return true;
                }
                int spawn = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE piglinbrutesettings SET Spawn = ? WHERE ID = 1");
                ps.setInt(1, spawn);
                ps.executeUpdate();
                sender.sendMessage("El spawn del PiglinBrute se ha establecido en " + spawn);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el spawn del PiglinBrute. Uso correcto: /PiglinBruteSetSpawn <spawn>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("PiglinBruteStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Spawn FROM piglinbrutesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int spawn = rs.getInt("Spawn");
                    sender.sendMessage("El spawn del PiglinBrute está en " + spawn);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del PiglinBrute. Uso correcto: /PiglinBruteStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
