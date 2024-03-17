package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MagmaCubeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("MagmaCubeSetKnockback")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer el empuje hacia atrás del MagmaCube. Uso correcto: /MagmaCubeSetKnockback <knockback>");
                    return true;
                }
                int knockback = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE magmacubesettings SET Knockback = ? WHERE ID = 1");
                ps.setInt(1, knockback);
                ps.executeUpdate();
                sender.sendMessage("El empuje hacia atrás del MagmaCube se ha establecido en " + knockback);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer el empuje hacia atrás del MagmaCube. Uso correcto: /MagmaCubeSetKnockback <knockback>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("MagmaCubeSetStrength")) {
            try {
                if (args.length != 1) {
                    sender.sendMessage("Error al establecer la fuerza del MagmaCube. Uso correcto: /MagmaCubeSetStrength <strenght>");
                    return true;
                }
                int strength = Integer.parseInt(args[0]);
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE magmacubesettings SET Strength = ? WHERE ID = 1");
                ps.setInt(1, strength);
                ps.executeUpdate();
                sender.sendMessage("La fuerza del MagmaCube se ha establecido en " + strength);
            } catch (Exception e) {
                sender.sendMessage("Error al establecer la fuerza del MagmaCube. Uso correcto: /MagmaCubeSetStrength <strenght>");
                e.printStackTrace();
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("MagmaCubeStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT Knockback, Strength FROM magmacubesettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int knockback = rs.getInt("Knockback");
                    int strength = rs.getInt("Strength");
                    sender.sendMessage("El empuje hacia atrás del MagmaCube está en " + knockback + " y la fuerza del MagmaCube está en " + strength);
                }
            } catch (Exception e) {
                sender.sendMessage("Error al obtener el estado del MagmaCube. Uso correcto: /MagmaCubeStatus");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
