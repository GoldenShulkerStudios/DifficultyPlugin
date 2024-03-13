package me.ewahv1.plugin.Commands.Storm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ewahv1.plugin.Listeners.Storm.StormListener;

public class ToggleStormCommand implements CommandExecutor {

    private StormListener stormListener;

    public ToggleStormCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getWorld().hasStorm()) {
                player.getWorld().setStorm(false);
                stormListener.setStormActive(false); // Desactiva la tormenta en el plugin
                stormListener.setStormTime(0); // Establece el tiempo de la tormenta en 0
                stormListener.hideBossBar(); // Oculta la bossbar
                player.sendMessage("La tormenta ha sido desactivada.");
            } else {
                stormListener.setStormActive(true); // Activa la tormenta en el plugin
                player.sendMessage("La tormenta ha sido activada. Comenzará cuando un jugador muera.");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
