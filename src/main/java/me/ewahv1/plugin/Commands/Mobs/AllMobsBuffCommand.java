package me.ewahv1.plugin.Commands.Mobs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.ewahv1.plugin.Listeners.Mobs.AllMobsBuffListener;

public class AllMobsBuffCommand implements CommandExecutor {

    private AllMobsBuffListener listener;

    public AllMobsBuffCommand(AllMobsBuffListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setAllMobsBuffGenericAttackDamage")) {
            if (args.length == 1) {
                try {
                    double multiplier = Double.parseDouble(args[0]);
                    listener.setAttackDamageMultiplier(multiplier);
                    sender.sendMessage("El multiplicador de daño de todos los mobs ha sido establecido a " + multiplier);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffGenericAttackDamage <multiplicador>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setAllMobsBuffSpeed")) {
            if (args.length == 1) {
                try {
                    int speedLevel = Integer.parseInt(args[0]);
                    listener.setSpeedLevel(speedLevel);
                    sender.sendMessage("El nivel de velocidad de todos los mobs ha sido establecido a " + speedLevel);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffSpeed <nivel>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setAllMobsBuffStrength")) {
            if (args.length == 1) {
                try {
                    int strengthLevel = Integer.parseInt(args[0]);
                    listener.setStrengthLevel(strengthLevel);
                    sender.sendMessage("El nivel de fuerza de todos los mobs ha sido establecido a " + strengthLevel);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffStrength <nivel>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setAllMobsBuffResistance")) {
            if (args.length == 1) {
                try {
                    int resistanceLevel = Integer.parseInt(args[0]);
                    listener.setResistanceLevel(resistanceLevel);
                    sender.sendMessage("El nivel de resistencia de todos los mobs ha sido establecido a " + resistanceLevel);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffResistance <nivel>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setAllMobsBuffRegeneration")) {
            if (args.length == 1) {
                try {
                    int regenerationLevel = Integer.parseInt(args[0]);
                    listener.setRegenerationLevel(regenerationLevel);
                    sender.sendMessage("El nivel de regeneración de todos los mobs ha sido establecido a " + regenerationLevel);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffRegeneration <nivel>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("setAllMobsBuffInvisibilityChance")) {
            if (args.length == 1) {
                try {
                    double invisibilityChance = Double.parseDouble(args[0]);
                    if (invisibilityChance < 0 || invisibilityChance > 100) {
                        sender.sendMessage("Por favor, introduce un número entre 0 y 100.");
                    } else {
                        listener.setInvisibilityChance(invisibilityChance / 100);
                        sender.sendMessage("La probabilidad de invisibilidad de todos los mobs ha sido establecida a " + invisibilityChance + "%");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("Por favor, introduce un número válido.");
                }
            } else {
                sender.sendMessage("Uso: /setAllMobsBuffInvisibilityChance <probabilidad>");
            }
            return true;
        }
        return false;
    }
}
