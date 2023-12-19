package me.ashydev.exampleplugin.database;

import me.ashydev.exampleplugin.user.User;

public interface Database<I, U extends User<I>> {

    void connect();
    void initialize();

    U load(I id);
    void save(U user);
}
