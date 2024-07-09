package me.ewahv1.plugin.Commands.Difficulty;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DifficultyTabCompleter implements TabCompleter {

    private static final List<String> SUB_COMMANDS = Arrays.asList("totem");

    private static final List<String> TOTEM_COMMANDS = Arrays.asList("toggle", "status", "setFail");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filterStartingWith(args[0], SUB_COMMANDS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("totem")) {
            return filterStartingWith(args[1], TOTEM_COMMANDS);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("totem") && args[1].equalsIgnoreCase("setFail")) {
            return null; // Si quieres permitir valores numéricos personalizados
        }

        return new ArrayList<>();
    }

    private List<String> filterStartingWith(String prefix, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(prefix.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
}
