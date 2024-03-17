package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ZombifiedPiglinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ZombifiedPiglinSetSpeed")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer la velocidad del Zombified Piglin. Uso correcto: /ZombifiedPiglinSetSpeed <velocidad>");
                return false;
            }
            int speed = Integer.parseInt(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE zombifiedpiglinsettings SET Speed = ? WHERE ID = 1");
                ps.setInt(1, speed);
                ps.executeUpdate();
                sender.sendMessage("Velocidad del Zombified Piglin establecida a " + speed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("ZombifiedPiglinToggle")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Weapon FROM zombifiedpiglinsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean weapon = rs.getBoolean("Weapon");
                    PreparedStatement psUpdate = Connection.getConnection().prepareStatement("UPDATE zombifiedpiglinsettings SET Weapon = ? WHERE ID = 1");
                    psUpdate.setBoolean(1, !weapon);
                    psUpdate.executeUpdate();
                    sender.sendMessage("Arma del Zombified Piglin establecida a " + (!weapon ? "Blaze Rod" : "Ninguna"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("ZombifiedPiglinSetFireAspect")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el Fire Aspect de la Blaze Rod del Zombified Piglin. Uso correcto: /ZombifiedPiglinSetFireAspect <nivel>");
                return false;
            }
            int fireAspect = Integer.parseInt(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE zombifiedpiglinsettings SET FireAspect = ? WHERE ID = 1");
                ps.setInt(1, fireAspect);
                ps.executeUpdate();
                sender.sendMessage("Fire Aspect de la Blaze Rod del Zombified Piglin establecido a " + fireAspect);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("ZombifiedPiglinStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT * FROM zombifiedpiglinsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int speed = rs.getInt("Speed");
                    boolean weapon = rs.getBoolean("Weapon");
                    int fireAspect = rs.getInt("FireAspect");
                    sender.sendMessage("Estado del Zombified Piglin: Velocidad = " + speed + ", Arma = " + (weapon ? "Blaze Rod" : "Ninguna") + ", Fire Aspect = " + fireAspect);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
