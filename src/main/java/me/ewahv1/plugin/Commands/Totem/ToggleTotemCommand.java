package me.ewahv1.plugin.Commands.Totem;

import me.ewahv1.plugin.Listeners.Items.FailTotemListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTotemCommand implements CommandExecutor {
    private FailTotemListener failTotemListener;

    public ToggleTotemCommand(FailTotemListener failTotemListener) {
        this.failTotemListener = failTotemListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean isTotemActive = failTotemListener.isTotemActive();
            failTotemListener.setTotemActive(!isTotemActive);
            player.sendMessage("La mecánica de failtotem ahora está " + (!isTotemActive ? "activada" : "desactivada") + ".");
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
