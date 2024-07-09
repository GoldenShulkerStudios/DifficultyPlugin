package me.ewahv1.plugin.Commands.Difficulty;

import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;
import me.ewahv1.plugin.Commands.Difficulty.Totem.*;
import me.ewahv1.plugin.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DifficultyCommand implements CommandExecutor {

    private final Main plugin;
    private final FailTotemListener failTotemListener;
    private final DatabaseConnection connection;

    public DifficultyCommand(Main plugin, FailTotemListener failTotemListener, DatabaseConnection connection) {
        this.plugin = plugin;
        this.failTotemListener = failTotemListener;
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Uso: /difficulty [comando]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "totem":
                if (args.length < 2) {
                    sender.sendMessage("Uso: /difficulty totem [comando]");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "toggle":
                        return new ToggleTotemCommand(failTotemListener, plugin, connection).onCommand(sender, command, label, args);
                    case "status":
                        return new TotemStatusCommand(failTotemListener, plugin, connection).onCommand(sender, command, label, args);
                    case "setfail":
                        return new SetFailTotemCommand(failTotemListener, plugin, connection).onCommand(sender, command, label, args);
                    default:
                        sender.sendMessage("Comando de totem desconocido: " + args[1]);
                        return true;
                }
            default:
                sender.sendMessage("Comando desconocido: " + args[0]);
                return true;
        }
    }
}
