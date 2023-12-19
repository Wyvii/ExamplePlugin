package me.ashydev.exampleplugin.config;

import me.ashydev.exampleplugin.ExamplePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public abstract class AbstractYamlConfiguration implements Configuration<YamlConfiguration> {
    private YamlConfiguration configuration;
    private final File file;
    private final String path;
    private boolean resource = false;

    public AbstractYamlConfiguration(final String path) {
        this.file = new File(
                ExamplePlugin.getInstance().getDataFolder(), path
        );

        this.path = path;
    }

    public AbstractYamlConfiguration(final String path, final boolean resource) {
        this.file = new File(
                ExamplePlugin.getInstance().getDataFolder(), path
        );

        this.path = path;
        this.resource = resource;
    }


    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save() {
        final ExamplePlugin plugin = ExamplePlugin.getInstance();

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
                initialize();

                if (resource) {
                    plugin.saveResource(path, true);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void reload() {
        save();
        load();
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
