package me.ewahv1.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import me.ewahv1.plugin.Commands.Storm.*;
import me.ewahv1.plugin.Commands.Totem.*;
import me.ewahv1.plugin.Commands.Mobs.AllMobsBuffCommand; // Importa la clase AllMobsBuffCommand
import me.ewahv1.plugin.Listeners.Mobs.*;
import me.ewahv1.plugin.Listeners.Storm.StormListener;
import me.ewahv1.plugin.Listeners.Items.FailTotemListener;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        StormListener stormListener = new StormListener(this);
        FailTotemListener failTotemListener = new FailTotemListener(this);

        getServer().getPluginManager().registerEvents(stormListener, this);
        getServer().getPluginManager().registerEvents(failTotemListener, this);

        getCommand("setstormtime").setExecutor(new SetStormTimeCommand(stormListener));
        getCommand("togglestorm").setExecutor(new ToggleStormCommand(stormListener));
        getCommand("resetstorm").setExecutor(new ResetStormCommand(stormListener));
        getCommand("stormstatus").setExecutor(new StormStatusCommand(stormListener));
        getCommand("setbasestormtime").setExecutor(new SetBaseStormTimeCommand(stormListener));

        getCommand("toggletotem").setExecutor(new ToggleTotemCommand(failTotemListener));
        getCommand("setfailtotem").setExecutor(new SetFailTotemCommand(failTotemListener));
        getCommand("totemstatus").setExecutor(new TotemStatusCommand(failTotemListener));

        AllMobsBuffListener allMobsBuffListener = new AllMobsBuffListener(); // Crea una instancia de AllMobsBuffListener
        getServer().getPluginManager().registerEvents(allMobsBuffListener, this);
        getCommand("setAllMobsBuffGenericAttackDamage").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));
        getCommand("setAllMobsBuffSpeed").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));
        getCommand("setAllMobsBuffStrength").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));
        getCommand("setAllMobsBuffResistance").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));
        getCommand("setAllMobsBuffRegeneration").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));
        getCommand("setAllMobsBuffInvisibilityChance").setExecutor(new AllMobsBuffCommand(allMobsBuffListener));

        getServer().getPluginManager().registerEvents(new BlazeListener(), this);
        getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
        getServer().getPluginManager().registerEvents(new GhastListener(), this);
        getServer().getPluginManager().registerEvents(new PhantomListener(), this);
        getServer().getPluginManager().registerEvents(new PillagerListener(), this);
        getServer().getPluginManager().registerEvents(new SkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new VexListener(), this);
        getServer().getPluginManager().registerEvents(new VindicatorListener(), this);
        getServer().getPluginManager().registerEvents(new WitherSkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new ZombieListener(), this);
    }
}
