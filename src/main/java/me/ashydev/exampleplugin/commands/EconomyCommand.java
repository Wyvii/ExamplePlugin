package me.ashydev.exampleplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.ashydev.exampleplugin.ExamplePlugin;
import me.ashydev.exampleplugin.user.BukkitUser;
import me.ashydev.exampleplugin.user.manager.BukkitUserManager;
import me.ashydev.exampleplugin.user.manager.UserManager;
import me.ashydev.exampleplugin.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("eco|economy")
@Description("Economy command")
public class EconomyCommand extends BaseCommand {

    @Subcommand("bal")
    @Description("shows you your balance")
    public void onBalanceCommand(Player player) {
        final UserManager<UUID, BukkitUser> userManager = ExamplePlugin.getInstance().getUserManager();
        final BukkitUser user = userManager.query(player.getUniqueId());

        player.sendMessage(ColorUtil.colorize("&6Your balance is: &c" + user.getGold()));
    }

    @Subcommand("transfer")
    @Description("transfer gold to another player")
    @CommandCompletion("@players")
    public void onTransferCommand(Player sender, String player, double amount) {
        final UserManager<UUID, BukkitUser> userManager = ExamplePlugin.getInstance().getUserManager();

        final Player receiver = Bukkit.getPlayer(player);           // this could be changed to work for offline players very easily

        if (receiver == null) {
            sender.sendMessage(ColorUtil.colorize("&cCould not find player"));
            return;
        }

        final BukkitUser userA = userManager.query(sender.getUniqueId());
        final BukkitUser userB = userManager.query(sender.getUniqueId());

        if (userA.getUniqueId() == userB.getUniqueId()) {
            sender.sendMessage(ColorUtil.colorize("&cYou cannot send gold to yourself"));
            return;
        }

        double gold = userA.getGold();

        if (gold < amount) {
            sender.sendMessage(ColorUtil.colorize("&cYou do not have enough gold"));
            return;
        }

        userA.setGold(gold - amount);
        userB.setGold(userB.getGold() + amount);

        receiver.sendMessage(ColorUtil.colorize("&6You received &a" + amount + " &6gold from &c" + sender.getDisplayName()));
        sender.sendMessage(ColorUtil.colorize("&6You sent &a" + amount + " &6gold to &c" + receiver.getDisplayName()));
    }

}
