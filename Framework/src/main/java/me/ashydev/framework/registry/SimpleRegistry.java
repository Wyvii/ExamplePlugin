package me.ashydev.framework.registry;

public class SimpleRegistry<K, V> extends AbstractRegistry<K, V> {
    public SimpleRegistry(RegistryType type, int capacity) {
        super(type, capacity);
    }

    public SimpleRegistry(RegistryType type) {
        super(type);
    }

    public SimpleRegistry() { }
}
