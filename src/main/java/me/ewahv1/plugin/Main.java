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
        getServer().getPluginManager().registerEvents(new AllMobsListener(), this);

        getCommand("setstormtime").setExecutor(new SetStormTimeCommand(stormListener));
        getCommand("togglestorm").setExecutor(new ToggleStormCommand(stormListener));
        getCommand("resetstorm").setExecutor(new ResetStormCommand(stormListener));
        getCommand("stormstatus").setExecutor(new StormStatusCommand(stormListener));
        getCommand("setbasestormtime").setExecutor(new SetBaseStormTimeCommand(stormListener));

        getCommand("toggletotem").setExecutor(new ToggleTotemCommand(failTotemListener));
        getCommand("setfailtotem").setExecutor(new SetFailTotemCommand(failTotemListener));
        getCommand("totemstatus").setExecutor(new TotemStatusCommand(failTotemListener));

        getCommand("BeeToggle").setExecutor(new BeeCommand());
        getCommand("BeeSetStrength").setExecutor(new BeeCommand());
        getCommand("BeeStatus").setExecutor(new BeeCommand());

        getCommand("BlazeToggle").setExecutor(new BlazeCommand());
        getCommand("BlazeStatus").setExecutor(new BlazeCommand());

        getCommand("CreeperSetExplosionSpeed").setExecutor(new CreeperCommand());
        getCommand("CreeperStatus").setExecutor(new CreeperCommand());

        getCommand("DrownedToggleTrident").setExecutor(new DrownedCommand());
        getCommand("DrownedStatus").setExecutor(new DrownedCommand());

        getCommand("ElderGuardianSetResistance").setExecutor(new ElderGuardianCommand());
        getCommand("ElderGuardianStatus").setExecutor(new ElderGuardianCommand());

        getCommand("EndermanSetSpeed").setExecutor(new EndermanCommand());
        getCommand("EndermanSetStrenght").setExecutor(new EndermanCommand());
        getCommand("EndermanStatus").setExecutor(new EndermanCommand());

        getCommand("EndermiteToggle").setExecutor(new EndermiteCommand());
        getCommand("EndermiteSetStrenght").setExecutor(new EndermiteCommand());
        getCommand("EndermiteStatus").setExecutor(new EndermiteCommand());

        getCommand("GhastSetExplosionPower").setExecutor(new GhastCommand());
        getCommand("GhastStatus").setExecutor(new GhastCommand());

        getCommand("GuardianSetResistance").setExecutor(new GuardianCommand());
        getCommand("GuardianStatus").setExecutor(new GuardianCommand());

        getCommand("HoglinSetKnockback").setExecutor(new HoglinCommand());
        getCommand("HoglinStatus").setExecutor(new GuardianCommand());

        getCommand("HuskSetAxe").setExecutor(new HuskCommand());
        getCommand("HuskSetSharpness").setExecutor(new HuskCommand());
        getCommand("HuskStatus").setExecutor(new GuardianCommand());

        getCommand("MagmaCubeSetKnockback").setExecutor(new MagmaCubeCommand());
        getCommand("MagmaCubeSetStrenght").setExecutor(new MagmaCubeCommand());
        getCommand("MagmaCubeStatus").setExecutor(new GuardianCommand());

        getCommand("PhantomSetSize").setExecutor(new PhantomCommand());
        getCommand("PhantomStatus").setExecutor(new GuardianCommand());

        getCommand("PiglinBruteSetSpawn").setExecutor(new PiglinBruteCommand());
        getCommand("PiglinBruteStatus").setExecutor(new GuardianCommand());

        getServer().getPluginManager().registerEvents(new BlazeListener(), this);
        getServer().getPluginManager().registerEvents(new BeeListener(), this);
        getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
        getServer().getPluginManager().registerEvents(new DrownedListener(), this);
        getServer().getPluginManager().registerEvents(new ElderGuardianListener(), this);
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EndermiteListener(), this);
        getServer().getPluginManager().registerEvents(new GhastListener(), this);
        getServer().getPluginManager().registerEvents(new GuardianListener(), this);
        getServer().getPluginManager().registerEvents(new HoglinListener(), this);
        getServer().getPluginManager().registerEvents(new HuskListener(), this);
        getServer().getPluginManager().registerEvents(new MagmaCubeListener(), this);
        getServer().getPluginManager().registerEvents(new PhantomListener(), this);
        getServer().getPluginManager().registerEvents(new PiglinBruteListener(), this);
        getServer().getPluginManager().registerEvents(new PillagerListener(), this);
        getServer().getPluginManager().registerEvents(new SkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new VexListener(), this);
        getServer().getPluginManager().registerEvents(new VindicatorListener(), this);
        getServer().getPluginManager().registerEvents(new WitherSkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new ZombieListener(), this);
    }
}