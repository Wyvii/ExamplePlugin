package me.ashydev.exampleplugin;

import co.aikar.commands.BukkitCommandManager;
import me.ashydev.exampleplugin.commands.EconomyCommand;
import me.ashydev.exampleplugin.config.DefaultConfiguration;
import me.ashydev.exampleplugin.database.ExampleDatabase;
import me.ashydev.exampleplugin.listener.PlayerStateListener;
import me.ashydev.exampleplugin.user.BukkitUser;
import me.ashydev.exampleplugin.user.manager.BukkitUserManager;
import me.ashydev.exampleplugin.user.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class ExamplePlugin extends JavaPlugin {
    private static ExamplePlugin instance;
    private UserManager<UUID, BukkitUser> userManager;
    private DefaultConfiguration config;
    private ExampleDatabase database;
    private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();

        database = new ExampleDatabase(config.getConfiguration().getString("database.url")); // not proud of this part, very messy
        database.connect();

        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new EconomyCommand());

        userManager = new BukkitUserManager(database);

        Bukkit.getPluginManager().registerEvents(new PlayerStateListener(), this);
    }

    void setupConfig() {
        this.config = new DefaultConfiguration();
        config.save();
        config.load();
    }

    @Override
    public void onDisable() {
        config.save();
        userManager.dump();
    }

    public UserManager<UUID, BukkitUser> getUserManager() {
        return userManager;
    }

    public static ExamplePlugin getInstance() {
        return instance;
    }
}
