package me.ashydev.exampleplugin.user.manager;

import com.github.benmanes.caffeine.cache.Cache;
import me.ashydev.exampleplugin.user.User;
import me.ashydev.framework.registry.Registry;

import java.util.Map;

public interface UserManager<I, T extends User<I>> {
    void load(I id);
    void save(I id);

    void register(T user);
    void cached(I user);

    T query(I user);

    void unregister(I user);

    boolean isRegistered(I id);
    boolean isCached(I id);

    boolean isValid(I id);

    void dump();

    Cache<I, T> getCache();
    Registry<I, T> getRegistry();
}
