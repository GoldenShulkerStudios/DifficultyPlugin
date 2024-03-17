package me.ewahv1.plugin.Commands.Mobs;

import me.ewahv1.plugin.Database.Connection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PillagerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("PillagerSetDamage")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el daño del Pillager. Uso correcto: /PillagerSetDamage <multiplicador>");
                return false;
            }
            double damageMultiplier = Double.parseDouble(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE pillagersettings SET DamageMultiplier = ? WHERE ID = 1");
                ps.setDouble(1, damageMultiplier);
                ps.executeUpdate();
                sender.sendMessage("Multiplicador de daño establecido a " + damageMultiplier);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("PillagerSetCriticalPorcentage")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el porcentaje crítico del Pillager. Uso correcto: /PillagerSetCriticalPorcentage <porcentaje>");
                return false;
            }
            double criticalChance = Double.parseDouble(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE pillagersettings SET CriticalChance = ? WHERE ID = 1");
                ps.setDouble(1, criticalChance);
                ps.executeUpdate();
                sender.sendMessage("Porcentaje crítico establecido a " + criticalChance);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("PillagerSetInstantDamageArrow")) {
            if (args.length != 1) {
                sender.sendMessage("Error al establecer el daño instantáneo de la flecha del Pillager. Uso correcto: /PillagerSetInstantDamageArrow <daño>");
                return false;
            }
            int instantDamage = Integer.parseInt(args[0]);
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("UPDATE pillagersettings SET InstantDamage = ? WHERE ID = 1");
                ps.setInt(1, instantDamage);
                ps.executeUpdate();
                sender.sendMessage("Daño instantáneo establecido a " + instantDamage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("PillagerStatus")) {
            try {
                PreparedStatement ps = Connection.getConnection().prepareStatement("SELECT * FROM pillagersettings WHERE ID = 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double damageMultiplier = rs.getDouble("DamageMultiplier");
                    double criticalChance = rs.getDouble("CriticalChance");
                    int instantDamage = rs.getInt("InstantDamage");
                    sender.sendMessage("Estado de Pillager: Daño = " + damageMultiplier + ", Crítico = " + criticalChance + ", Daño Instantáneo = " + instantDamage);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
