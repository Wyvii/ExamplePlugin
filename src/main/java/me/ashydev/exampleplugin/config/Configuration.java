package me.ashydev.exampleplugin.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface Configuration<S extends FileConfiguration> {
    void initialize();

    void load();
    void save();

    void reload();

    S getConfiguration();
}
