package me.ewahv1.plugin.Commands.Storm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ewahv1.plugin.Listeners.Storm.StormListener;

public class SetBaseStormTimeCommand implements CommandExecutor {

    private StormListener stormListener;

    public SetBaseStormTimeCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                try {
                    int baseStormTime = Integer.parseInt(args[0]);
                    stormListener.setBaseStormTime(baseStormTime);
                    player.sendMessage("El tiempo base de la tormenta se ha establecido en " + baseStormTime + " segundos.");
                } catch (NumberFormatException e) {
                    player.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                player.sendMessage("Uso: /setbasestormtime <segundos>");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
