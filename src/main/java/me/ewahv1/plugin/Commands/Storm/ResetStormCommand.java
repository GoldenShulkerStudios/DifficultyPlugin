package me.ewahv1.plugin.Commands.Storm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ewahv1.plugin.Listeners.Storm.StormListener;

public class ResetStormCommand implements CommandExecutor {

    private StormListener stormListener;

    public ResetStormCommand(StormListener stormListener) {
        this.stormListener = stormListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            stormListener.setStormTime(0); // Restablece el tiempo de la tormenta a 0
            stormListener.setBaseStormTime(120); // Restablece el tiempo base de la tormenta a su valor original
            stormListener.hideBossBar(); // Oculta la bossbar
            player.getWorld().setStorm(false); // Detiene la tormenta
            player.sendMessage("La tormenta ha sido reiniciada.");
        } else {
            sender.sendMessage("Este comando solo puede ser utilizado por un jugador.");
        }
        return true;
    }
}
