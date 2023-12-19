package me.ashydev.framework.registry;

public class StringRegistry<T> extends AbstractRegistry<String, T> {
    public StringRegistry(RegistryType type, int capacity) {
        super(type, capacity);
    }

    public StringRegistry(RegistryType type) {
        super(type);
    }

    public StringRegistry() {
        super();
    }
}
