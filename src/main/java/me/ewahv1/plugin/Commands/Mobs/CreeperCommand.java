package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;

public class CreeperCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setCreeperExplosionSpeed")) {
            try {
                double explosionSpeed = Double.parseDouble(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE creepersettings SET ExplosionSpeed = ? WHERE ID = 1");
                ps.setDouble(1, explosionSpeed);
                ps.executeUpdate();
                sender.sendMessage("La reduccion de velocidad de explocion se multiplica por " + explosionSpeed);
            } catch (Exception e) {
                sender.sendMessage("Error al colocar la velocidad de explosion del creeper");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
