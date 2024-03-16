package me.ewahv1.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import me.ewahv1.plugin.Commands.Storm.*;
import me.ewahv1.plugin.Commands.Totem.*;
import me.ewahv1.plugin.Commands.Mobs.*;
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

        getCommand("toggleblaze").setExecutor(new BlazeCommand());
        getCommand("blazestatus").setExecutor(new BlazeCommand());

        getCommand("setbeehostility").setExecutor(new BeeCommand());
        getCommand("setbeestrength").setExecutor(new BeeCommand());

        getCommand("setcreeperexplosionspeed").setExecutor(new CreeperCommand());

        getCommand("setdrownedtrident").setExecutor(new DrownedCommand());
        getCommand("setdrownedchanneling").setExecutor(new DrownedCommand());

        getCommand("setelderguardianresistance").setExecutor(new ElderGuardianCommand());

        getCommand("setendermanspeed").setExecutor(new EndermanCommand());
        getCommand("setendermanstrength").setExecutor(new EndermanCommand());

        getCommand("setendermiteinvulnerability").setExecutor(new EndermiteCommand());
        getCommand("setendermitestrength").setExecutor(new EndermiteCommand());

        getCommand("setghastexplosionpower").setExecutor(new GhastCommand());

        getServer().getPluginManager().registerEvents(new BlazeListener(), this);
        getServer().getPluginManager().registerEvents(new BeeListener(), this);
        getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
        getServer().getPluginManager().registerEvents(new DrownedListener(), this);
        getServer().getPluginManager().registerEvents(new ElderGuardianListener(), this);
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EndermiteListener(), this);
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