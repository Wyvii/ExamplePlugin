package me.ashydev.framework.registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistry<K, V> implements Registry<K, V> {
    private final Map<K, V> map;
    private int capacity = -1;

    public AbstractRegistry(RegistryType type, int capacity) {
        map = switch (type) {
            case WEAK -> new WeakHashMap<>(capacity);
            case NORMAL -> new HashMap<>(capacity);
            case CONCURRENT -> new ConcurrentHashMap<>(capacity);
        };

        this.capacity = capacity;
    }

    public AbstractRegistry(RegistryType type) {
        map = switch (type) {
            case WEAK -> new WeakHashMap<>();
            case NORMAL -> new HashMap<>();
            case CONCURRENT -> new ConcurrentHashMap<>();
        };
    }

    public AbstractRegistry() {
        this(RegistryType.NORMAL);
    }

    @Override
    public void register(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V query(K key) {
        return map.get(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public void clean() {
        map.clear();
    }

    @Override
    public boolean registered(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public Map<K, V> getMap() {
        return map;
    }

    public enum RegistryType {
        WEAK, CONCURRENT, NORMAL
    }
}
