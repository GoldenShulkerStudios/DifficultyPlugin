package me.ewahv1.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ewahv1.plugin.Database.DatabaseConnection;
import me.ewahv1.plugin.Database.DatabaseConfig;
import me.ewahv1.plugin.Listeners.DayListener;
import me.ewahv1.plugin.Listeners.Difficulty.Items.FailTotemListener;
import me.ewahv1.plugin.Commands.Difficulty.Totem.*;
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
        // Cargar o crear la configuración de la base de datos
        File configFile = new File(getDataFolder(), "database-config.json");
        DatabaseConfig config = loadOrCreateConfig(configFile);

        // Inicializar la conexión a la base de datos
        connection = new DatabaseConnection(config.getUrl(), config.getUsername(), config.getPassword());

        // Obtener el día actual de manera asincrónica
        DayListener.getCurrentDayAsync(connection).thenAccept(currentDay -> {
            getLogger().info("El día actual es: " + currentDay);
        });

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
        PhantomListener phantomListener = new PhantomListener();
        PiglinListener piglinListener = new PiglinListener();
        PillagerListener pillagerListener = new PillagerListener();
        RavagerListener ravagerListener = new RavagerListener();
        SilverfishListener silverfishListener = new SilverfishListener();
        SkeletonListener skeletonListener = new SkeletonListener();
        SlimeListener slimeListener = new SlimeListener();
        StrayListener strayListener = new StrayListener();
        VexListener vexListener = new VexListener();
        VindicatorListener vindicatorListener = new VindicatorListener();
        WitchListener witchListener = new WitchListener();
        WitherSkeletonListener witherSkeletonListener = new WitherSkeletonListener();
        ZoglinListener zoglinListener = new ZoglinListener();
        ZombieListener zombieListener = new ZombieListener();
        ZombieVillagerListener zombieVillagerListener = new ZombieVillagerListener();
        ZombifiedPiglinListener zombifiedPiglinListener = new ZombifiedPiglinListener();

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

        getCommand("toggletotem").setExecutor(new ToggleTotemCommand(failTotemListener, this, connection));
        getCommand("totemstatus").setExecutor(new TotemStatusCommand(failTotemListener));
        getCommand("setfailtotem").setExecutor(new SetFailTotemCommand(failTotemListener, this, connection));
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
            defaultConfig.setUrl("jdbc:mysql://localhost:3306/stormplugindb");
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
