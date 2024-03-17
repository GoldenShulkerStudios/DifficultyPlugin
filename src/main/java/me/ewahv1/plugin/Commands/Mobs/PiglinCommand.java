package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PiglinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("PiglinSetSwordMaterial")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el material de la espada del Piglin. Uso correcto: /PiglinSetSwordMaterial <material>");
                return false;
            }
            int swordMaterial = Integer.parseInt(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE piglinsettings SET SwordMaterial = ? WHERE ID = 1");
                ps.setInt(1, swordMaterial);
                ps.executeUpdate();
                sender.sendMessage("Material de la espada del Piglin establecido a " + swordMaterial);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("PiglinSetQuickCharge")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el Quick Charge de la ballesta del Piglin. Uso correcto: /PiglinSetQuickCharge <nivel>");
                return false;
            }
            int quickCharge = Integer.parseInt(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE piglinsettings SET QuickCharge = ? WHERE ID = 1");
                ps.setInt(1, quickCharge);
                ps.executeUpdate();
                sender.sendMessage("Quick Charge de la ballesta del Piglin establecido a " + quickCharge);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("PiglinStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT * FROM piglinsettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int swordMaterial = rs.getInt("SwordMaterial");
                    int quickCharge = rs.getInt("QuickCharge");
                    sender.sendMessage("Estado del Piglin: Material de la espada = " + swordMaterial + ", Quick Charge = " + quickCharge);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
