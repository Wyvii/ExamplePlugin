package me.ashydev.exampleplugin.user.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.ashydev.exampleplugin.database.ExampleDatabase;
import me.ashydev.exampleplugin.user.BukkitUser;
import me.ashydev.framework.registry.AbstractRegistry;
import me.ashydev.framework.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BukkitUserManager implements UserManager<UUID, BukkitUser> {
    private final Registry<UUID, BukkitUser> registry = Registry.allocate(AbstractRegistry.RegistryType.CONCURRENT);
    private final Cache<UUID, BukkitUser> cache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)              // should be moved to a config if this was a full plugin
            .maximumSize(10_000)                                         // same with this
            .build();

    private final ExampleDatabase database;

    public BukkitUserManager(ExampleDatabase database) {
        this.database = database;

        for (Player player : Bukkit.getOnlinePlayers()) {
            load(player.getUniqueId());
        }
    }

    @Override
    public void load(UUID id) {
        final BukkitUser user = database.load(id);

        register(user);
    }

    @Override
    public void save(UUID id) {
        final BukkitUser user = query(id);

        database.save(user);
    }

    @Override
    public void register(final BukkitUser user) {
        final UUID uuid = user.getUniqueId();

        if (!registry.registered(uuid)) registry.register(uuid, user);
    }

    @Override
    public void cached(final UUID user) {
        final ConcurrentMap<UUID, BukkitUser> cache = getCacheMap();

        if (!cache.containsKey(user))
            throw new IllegalArgumentException("Could not find user " + user + " in the cache, does it exist?");

        final BukkitUser cached = cache.get(user);

        register(cached);
    }

    @Override
    public BukkitUser query(final UUID user) {
        final ConcurrentMap<UUID, BukkitUser> cache = getCacheMap();

        if (!isRegistered(user) && !isCached(user))
            throw new RuntimeException("User " + user + " was not cached or registered to the registry, was it registered?");

        if (isRegistered(user))
            return registry.query(user);

        return cache.get(user);
    }

    @Override
    public void unregister(final UUID user) {
        if (!registry.registered(user))
            throw new IllegalArgumentException("Could not find user " + user + " in the registry, is it registered?");

        final BukkitUser registeredUser = registry.query(user);

        registry.remove(user);

        cache.put(user, registeredUser);
    }

    @Override
    public boolean isRegistered(final UUID id) {
        return registry.registered(id);
    }

    @Override
    public boolean isCached(final UUID id) {
        return getCacheMap().containsKey(id);
    }

    @Override
    public boolean isValid(final UUID id) {
        return isRegistered(id) || isCached(id);
    }

    @Override
    public void dump() {
        registry.getMap().values().forEach(user -> {
            save(user.getUniqueId());
        });

        cache.invalidateAll();
        registry.clean();
    }

    @Override
    public Cache<UUID, BukkitUser> getCache() {
        return cache;
    }

    @Override
    public Registry<UUID, BukkitUser> getRegistry() {
        return registry;
    }

    private ConcurrentMap<UUID, BukkitUser> getCacheMap() {
        return cache.asMap();
    }
}
