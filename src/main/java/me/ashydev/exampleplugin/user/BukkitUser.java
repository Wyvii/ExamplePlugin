package me.ashydev.exampleplugin.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class BukkitUser implements User<UUID> {
    public static BukkitUser allocate(UUID uuid) {
        return new BukkitUser(uuid, 0.0, 0, 0);
    }

    private transient final OfflinePlayer cached;

    private final UUID uuid;

    private double gold;
    private int kills, deaths;

    public BukkitUser(UUID uuid, double gold, int kills, int deaths) {
        this.uuid = uuid;
        this.gold = gold;

        cached = Bukkit.getOfflinePlayer(uuid);
    }


    public OfflinePlayer getAsOfflinePlayer() {
        return cached;
    }

    public boolean isOnline() {
        return cached.isOnline();
    }

    public Optional<Player> getAsPlayer() {
        if (!isOnline())
            return Optional.empty();

        Player player = cached.getPlayer();

        assert player != null;  // I wouldn't really do this normally but in this situation I would rather do this than suppress the warning

        return Optional.of(player);
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }
}
