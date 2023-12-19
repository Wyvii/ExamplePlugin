package me.ashydev.exampleplugin.utils;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
