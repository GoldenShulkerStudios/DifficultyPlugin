package me.ewahv1.plugin.Commands.Totem;

import me.ewahv1.plugin.Listeners.Items.FailTotemListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TotemStatusCommand implements CommandExecutor {
    private FailTotemListener failTotemListener;

    public TotemStatusCommand(FailTotemListener failTotemListener) {
        this.failTotemListener = failTotemListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("La mecánica de failtotem actualmente está " + (failTotemListener.isTotemActive() ? "activada" : "desactivada") + ".");
            player.sendMessage("El porcentaje de falla del totem está establecido en " + failTotemListener.getFailProbability() + "%.");
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
