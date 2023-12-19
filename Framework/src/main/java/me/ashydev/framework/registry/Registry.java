package me.ashydev.framework.registry;

import java.util.Map;
import java.util.Set;

public interface Registry<K, V> {
    static <K, V> Registry<K, V> allocate() {
        return new SimpleRegistry<>();
    }

    static <K, V> Registry<K, V> allocate(AbstractRegistry.RegistryType type) {
        return new SimpleRegistry<>(type);
    }

    static <K, V> Registry<K, V> allocate(int capacity) {
        return new SimpleRegistry<>(AbstractRegistry.RegistryType.NORMAL, capacity);
    }

    static <K, V> Registry<K, V> allocate(AbstractRegistry.RegistryType type, int capacity) {
        return new SimpleRegistry<>(type, capacity);
    }

    void register(K key, V value);

    V query(K key);
    void remove(K key);

    void clean();


    boolean registered(K key);
    boolean containsValue(V value);

    int size();
    int capacity();

    Map<K, V> getMap();
}
