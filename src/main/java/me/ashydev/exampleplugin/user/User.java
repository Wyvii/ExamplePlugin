package me.ashydev.exampleplugin.user;

import java.io.Serializable;

public interface User<I> extends Serializable {
    I getUniqueId();
}
