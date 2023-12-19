package me.ashydev.exampleplugin.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultConfiguration extends AbstractYamlConfiguration {
    public DefaultConfiguration() {
        super("config.yml", true);
    }

    @Override
    public void initialize() { }
}
