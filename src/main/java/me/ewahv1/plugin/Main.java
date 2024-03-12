package me.ewahv1.plugin;

import org.bukkit.plugin.java.JavaPlugin;
//import me.ewahv1.plugin.Listeners.DifficultityOnePlayers;
//import me.ewahv1.plugin.Listeners.StormListener;
import me.ewahv1.plugin.Listeners.Mobs.AllMobsListener;
import me.ewahv1.plugin.Listeners.Mobs.BlazeListener;
import me.ewahv1.plugin.Listeners.Mobs.CreeperListener;
import me.ewahv1.plugin.Listeners.Mobs.GhastListener;
import me.ewahv1.plugin.Listeners.Mobs.PhantomListener;
import me.ewahv1.plugin.Listeners.Mobs.SkeletonListener;
import me.ewahv1.plugin.Listeners.Mobs.VexListener;
import me.ewahv1.plugin.Listeners.Mobs.WitherSkeletonListener;
import me.ewahv1.plugin.Listeners.Mobs.ZombieListener;
//import me.ewahv1.plugin.Commands.StormCommand;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        //StormListener stormListener = new StormListener(this);
        //getServer().getPluginManager().registerEvents(stormListener, this);

        
        //getCommand("stormtime").setExecutor(new StormCommand(stormListener));

        //getServer().getPluginManager().registerEvents(new DifficultityOnePlayers(this), this);
        getServer().getPluginManager().registerEvents(new AllMobsListener(), this);
        getServer().getPluginManager().registerEvents(new BlazeListener(), this);
        getServer().getPluginManager().registerEvents(new CreeperListener(), this);
        getServer().getPluginManager().registerEvents(new GhastListener(), this);
        getServer().getPluginManager().registerEvents(new PhantomListener(), this);
        getServer().getPluginManager().registerEvents(new SkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new VexListener(), this);
        getServer().getPluginManager().registerEvents(new WitherSkeletonListener(), this);
        getServer().getPluginManager().registerEvents(new ZombieListener(), this);
    }
}
