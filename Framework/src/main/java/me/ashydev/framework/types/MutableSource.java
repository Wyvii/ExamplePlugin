package me.ashydev.framework.types;

public interface MutableSource<T> extends Source<T> {
    void set(T value);
}
