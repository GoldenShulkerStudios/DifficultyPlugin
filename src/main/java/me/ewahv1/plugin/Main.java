package me.ewahv1.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import me.ewahv1.plugin.Database.Connection;

import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;
import me.ewahv1.plugin.Listeners.Difficulty.Storm.StormListener;

import me.ewahv1.plugin.Commands.Difficulty.Storm.*;
import me.ewahv1.plugin.Commands.Difficulty.Totem.*;
import me.ewahv1.plugin.Listeners.Difficulty.Mobs.*;

import me.ewahv1.plugin.Listeners.Trinkets.*;


public class Main extends JavaPlugin {

    private Connection connection;

    @Override
    public void onEnable() {
        connection = new Connection();
        StormListener stormListener = new StormListener(this);
        FailTotemListener failTotemListener = new FailTotemListener(this);
        BeeListener beeListener = new BeeListener();
        BlazeListener blazeListener = new BlazeListener();
        CreeperListener creeperListener = new CreeperListener(this);
        DrownedListener drownedListener = new DrownedListener();
        ElderGuardianListener elderGuardianListener = new ElderGuardianListener();
        EndermanListener endermanListener = new EndermanListener();
        EndermiteListener endermiteListener = new EndermiteListener();
        GhastListener ghastListener = new GhastListener();
        GuardianListener guardianListener = new GuardianListener();
        HoglinListener hoglinListener = new HoglinListener();
        IronGolemListener ironGolemListener = new IronGolemListener();
        PiglinListener piglinListener = new PiglinListener();

        getServer().getPluginManager().registerEvents(stormListener, this);
        getServer().getPluginManager().registerEvents(failTotemListener, this);
        getServer().getPluginManager().registerEvents(beeListener, this); 
        getServer().getPluginManager().registerEvents(blazeListener, this); 
        getServer().getPluginManager().registerEvents(creeperListener, this);
        getServer().getPluginManager().registerEvents(drownedListener, this); 
        getServer().getPluginManager().registerEvents(elderGuardianListener, this); 
        getServer().getPluginManager().registerEvents(endermanListener, this); 
        getServer().getPluginManager().registerEvents(endermiteListener, this); 
        getServer().getPluginManager().registerEvents(ghastListener, this); 
        getServer().getPluginManager().registerEvents(guardianListener, this); 
        getServer().getPluginManager().registerEvents(hoglinListener, this); 
        getServer().getPluginManager().registerEvents(ironGolemListener, this); 
        getServer().getPluginManager().registerEvents(piglinListener, this); 

        getServer().getPluginManager().registerEvents(new BrazoPutrefactoListener(), this);
        getServer().getPluginManager().registerEvents(new DescalcificadorListener(), this);
        getServer().getPluginManager().registerEvents(new MochilaAntigravedadDefectuosaListener(), this);
        getServer().getPluginManager().registerEvents(new WarmogListener(this), this);


        getCommand("setstormtime").setExecutor(new SetStormTimeCommand(stormListener));
        getCommand("togglestorm").setExecutor(new ToggleStormCommand(stormListener));
        getCommand("resetstorm").setExecutor(new ResetStormCommand(stormListener));
        getCommand("stormstatus").setExecutor(new StormStatusCommand(stormListener));
        getCommand("setbasestormtime").setExecutor(new SetBaseStormTimeCommand(stormListener));

        getCommand("toggletotem").setExecutor(new ToggleTotemCommand(failTotemListener));
        getCommand("setfailtotem").setExecutor(new SetFailTotemCommand(failTotemListener));
        getCommand("totemstatus").setExecutor(new TotemStatusCommand(failTotemListener));

    }

    @Override
    public void onDisable() {
        connection.close();
    }
}
