package me.ewahv1.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Database.DatabaseConfig;
import me.ewahv1.plugin.Listeners.DayListener;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;
import me.ewahv1.plugin.Commands.Difficulty.DifficultyCommand;
import me.ewahv1.plugin.Commands.Difficulty.DifficultyTabCompleter;
import me.ewahv1.plugin.Listeners.Difficulty.Mobs.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JavaPlugin {

    private DatabaseConnection connection;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "database-config.json");
        DatabaseConfig config = loadOrCreateConfig(configFile);

        connection = new DatabaseConnection(config.getUrl(), config.getUsername(), config.getPassword());

        DayListener.init(this, connection);

        FailTotemListener failTotemListener = new FailTotemListener(this, connection);
        BeeListener beeListener = new BeeListener(connection);
        BlazeListener blazeListener = new BlazeListener(connection);
        CreeperListener creeperListener = new CreeperListener(this, connection);
        DrownedListener drownedListener = new DrownedListener(connection);
        ElderGuardianListener elderGuardianListener = new ElderGuardianListener(connection);
        EndermanListener endermanListener = new EndermanListener(connection);
        EndermiteListener endermiteListener = new EndermiteListener(connection);
        GhastListener ghastListener = new GhastListener(connection);
        GuardianListener guardianListener = new GuardianListener(connection);
        HoglinListener hoglinListener = new HoglinListener(connection);
        IronGolemListener ironGolemListener = new IronGolemListener(connection);
        PhantomListener phantomListener = new PhantomListener(connection);
        PiglinListener piglinListener = new PiglinListener(connection);
        PillagerListener pillagerListener = new PillagerListener(connection);
        RavagerListener ravagerListener = new RavagerListener(connection);
        SilverfishListener silverfishListener = new SilverfishListener();
        SkeletonListener skeletonListener = new SkeletonListener(connection);
        SlimeListener slimeListener = new SlimeListener(connection);
        StrayListener strayListener = new StrayListener(connection);
        VexListener vexListener = new VexListener(connection);
        VindicatorListener vindicatorListener = new VindicatorListener(connection);
        WitchListener witchListener = new WitchListener(connection);
        WitherSkeletonListener witherSkeletonListener = new WitherSkeletonListener(connection);
        ZoglinListener zoglinListener = new ZoglinListener(connection);
        ZombieListener zombieListener = new ZombieListener(connection);
        ZombieVillagerListener zombieVillagerListener = new ZombieVillagerListener(connection);
        ZombifiedPiglinListener zombifiedPiglinListener = new ZombifiedPiglinListener(connection);

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
        getServer().getPluginManager().registerEvents(phantomListener, this);
        getServer().getPluginManager().registerEvents(piglinListener, this);
        getServer().getPluginManager().registerEvents(pillagerListener, this);
        getServer().getPluginManager().registerEvents(ravagerListener, this);
        getServer().getPluginManager().registerEvents(silverfishListener, this);
        getServer().getPluginManager().registerEvents(skeletonListener, this);
        getServer().getPluginManager().registerEvents(slimeListener, this);
        getServer().getPluginManager().registerEvents(strayListener, this);
        getServer().getPluginManager().registerEvents(vexListener, this);
        getServer().getPluginManager().registerEvents(vindicatorListener, this);
        getServer().getPluginManager().registerEvents(witchListener, this);
        getServer().getPluginManager().registerEvents(witherSkeletonListener, this);
        getServer().getPluginManager().registerEvents(zoglinListener, this);
        getServer().getPluginManager().registerEvents(zombieListener, this);
        getServer().getPluginManager().registerEvents(zombieVillagerListener, this);
        getServer().getPluginManager().registerEvents(zombifiedPiglinListener, this);

        getCommand("difficulty").setExecutor(new DifficultyCommand(this, failTotemListener, connection));
        getCommand("difficulty").setTabCompleter(new DifficultyTabCompleter());
    }

    @Override
    public void onDisable() {
        connection.close();
    }

    private DatabaseConfig loadOrCreateConfig(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            DatabaseConfig defaultConfig = new DatabaseConfig();
            defaultConfig.setUrl("jdbc:mysql://localhost:3306/difficultyplugindb");
            defaultConfig.setUsername("root");
            defaultConfig.setPassword("root");
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(defaultConfig, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defaultConfig;
        } else {
            try (FileReader reader = new FileReader(file)) {
                return gson.fromJson(reader, DatabaseConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
