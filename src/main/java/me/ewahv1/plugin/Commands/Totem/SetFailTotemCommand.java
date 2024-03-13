package me.ewahv1.plugin.Commands.Totem;

import me.ewahv1.plugin.Listeners.Items.FailTotemListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetFailTotemCommand implements CommandExecutor {
    private FailTotemListener failTotemListener;

    public SetFailTotemCommand(FailTotemListener failTotemListener) {
        this.failTotemListener = failTotemListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Por favor, proporciona un porcentaje.");
            return false;
        }

        try {
            int percentage = Integer.parseInt(args[0]);
            if (percentage < 0 || percentage > 100) {
                sender.sendMessage("El porcentaje debe estar entre 0 y 100.");
                return false;
            }

            failTotemListener.setFailProbability(percentage);
            sender.sendMessage("El porcentaje de falla del totem ahora está establecido en " + percentage + "%.");
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Por favor, proporciona un número válido.");
            return false;
        }
    }
}
