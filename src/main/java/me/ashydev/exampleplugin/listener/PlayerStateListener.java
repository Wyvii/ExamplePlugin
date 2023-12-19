package me.ashydev.exampleplugin.listener;

import me.ashydev.exampleplugin.ExamplePlugin;
import me.ashydev.exampleplugin.user.BukkitUser;
import me.ashydev.exampleplugin.user.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerStateListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final UserManager<UUID, BukkitUser> userManager = ExamplePlugin.getInstance().getUserManager();
        final UUID uuid = event.getPlayer().getUniqueId();

        if (userManager.isCached(uuid)) {
            userManager.cached(uuid);

            return;
        }

        userManager.load(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final UserManager<UUID, BukkitUser> userManager = ExamplePlugin.getInstance().getUserManager();

        userManager.save(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final UserManager<UUID, BukkitUser> userManager = ExamplePlugin.getInstance().getUserManager();
        final Player player = event.getEntity();
        final BukkitUser user = userManager.query(player.getUniqueId());

        if (player.getKiller() != null) {
            final BukkitUser killer = userManager.query(player.getKiller().getUniqueId());

            killer.setGold(killer.getGold() + 100.0);
            killer.setKills(killer.getKills() + 1);
        }

        user.setDeaths(user.getDeaths() + 1);
        user.setGold(user.getGold() - 100.0);
    }
}
