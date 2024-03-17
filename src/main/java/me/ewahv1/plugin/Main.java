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
        getCommand("EndermanSetStrength").setExecutor(new EndermanCommand());
        getCommand("EndermanStatus").setExecutor(new EndermanCommand());

        getCommand("EndermiteToggle").setExecutor(new EndermiteCommand());
        getCommand("EndermiteSetStrength").setExecutor(new EndermiteCommand());
        getCommand("EndermiteStatus").setExecutor(new EndermiteCommand());

        getCommand("GhastSetExplosionPower").setExecutor(new GhastCommand());
        getCommand("GhastStatus").setExecutor(new GhastCommand());

        getCommand("GuardianSetResistance").setExecutor(new GuardianCommand());
        getCommand("GuardianStatus").setExecutor(new GuardianCommand());

        getCommand("HoglinSetKnockback").setExecutor(new HoglinCommand());
        getCommand("HoglinStatus").setExecutor(new HoglinCommand());

        getCommand("HuskSetAxe").setExecutor(new HuskCommand());
        getCommand("HuskSetSharpness").setExecutor(new HuskCommand());
        getCommand("HuskStatus").setExecutor(new HuskCommand());

        getCommand("IronGolemToggle").setExecutor(new IronGolemCommand());
        getCommand("IronGolemStatus").setExecutor(new IronGolemCommand());

        getCommand("MagmaCubeSetKnockback").setExecutor(new MagmaCubeCommand());
        getCommand("MagmaCubeSetStrength").setExecutor(new MagmaCubeCommand());
        getCommand("MagmaCubeStatus").setExecutor(new MagmaCubeCommand());

        getCommand("PhantomSetSize").setExecutor(new PhantomCommand());
        getCommand("PhantomStatus").setExecutor(new PhantomCommand());

        getCommand("PiglinBruteSetSpawn").setExecutor(new PiglinBruteCommand());
        getCommand("PiglinBruteStatus").setExecutor(new PiglinBruteCommand());

        getCommand("PiglinSetSwordMaterial").setExecutor(new PiglinCommand());
        getCommand("PiglinSetSwordMaterial").setExecutor(new PiglinCommand());
        getCommand("PiglinSetSwordMaterial").setExecutor(new PiglinCommand());
        getCommand("PiglinSetSwordMaterial").setExecutor(new PiglinCommand());

        getCommand("PillagerSetDamage").setExecutor(new PillagerCommand());
        getCommand("PiglinSetQuickCharge").setExecutor(new PillagerCommand());
        getCommand("PiglinStatus").setExecutor(new PillagerCommand());

        getCommand("ZombifiedPiglinSetSpeed").setExecutor(new ZombifiedPiglinCommand());
        getCommand("ZombifiedPiglinToggle").setExecutor(new ZombifiedPiglinCommand());
        getCommand("ZombifiedPiglinSetFireAspect").setExecutor(new ZombifiedPiglinCommand());

        getServer().getPluginManager().registerEvents(new BeeListener(), this);
        getServer().getPluginManager().registerEvents(new BlazeListener(), this);
        getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
        getServer().getPluginManager().registerEvents(new DrownedListener(), this);
        getServer().getPluginManager().registerEvents(new ElderGuardianListener(), this);
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EndermiteListener(), this);
        getServer().getPluginManager().registerEvents(new GhastListener(), this);
        getServer().getPluginManager().registerEvents(new GuardianListener(), this);
        getServer().getPluginManager().registerEvents(new HoglinListener(), this);
        getServer().getPluginManager().registerEvents(new HuskListener(), this);
        getServer().getPluginManager().registerEvents(new IronGolemListener(), this);
        getServer().getPluginManager().registerEvents(new MagmaCubeListener(), this);
        getServer().getPluginManager().registerEvents(new PhantomListener(), this);
        getServer().getPluginManager().registerEvents(new PiglinBruteListener(), this);
        getServer().getPluginManager().registerEvents(new PiglinListener(), this);
        getServer().getPluginManager().registerEvents(new PillagerListener(), this);
        getServer().getPluginManager().registerEvents(new ZombifiedPiglinListener(), this);
    }
}