package me.ewahv1.plugin.Commands.Storm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ewahv1.plugin.Listeners.Storm.StormListener;

public class StormStatusCommand implements CommandExecutor {

    private StormListener stormListener;

    public StormStatusCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getWorld().hasStorm()) {
                int remainingTime = stormListener.getStormTime();
                player.sendMessage("Actualmente hay una tormenta en tu mundo. Quedan " + remainingTime + " segundos.");
            } else {
                player.sendMessage("Actualmente no hay una tormenta en tu mundo.");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
