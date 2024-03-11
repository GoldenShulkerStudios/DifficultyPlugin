package me.ewahv1.plugin.Commands;

import me.ewahv1.plugin.Listeners.StormListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StormCommand implements CommandExecutor {

    private StormListener stormListener;

    public StormCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                try {
                    int stormTime = Integer.parseInt(args[0]);
                    stormListener.setStormTime(stormTime);
                    player.sendMessage("El tiempo de la tormenta se ha establecido en " + stormTime + " segundos.");
                } catch (NumberFormatException e) {
                    player.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                player.sendMessage("Uso: /stormtime <segundos>");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
