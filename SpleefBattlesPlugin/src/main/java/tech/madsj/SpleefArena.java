package tech.madsj;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SpleefArena {

    private Location position;
    private int size;
    private Map<UUID, Location> playerPrevPos;

    public SpleefArena(int size) {
        this.position = SpleefArenaCoordinator.getArenaLocation();
        this.size = size;
        this.playerPrevPos = new HashMap<>();
    }

    public void create() {

    }

    public void destroy() {

    }

    public void teleportPlayer(Player player) {
        playerPrevPos.put(player.getUniqueId(), player.getLocation());
    }

    public void returnPlayer(Player player) {
        player.teleport(playerPrevPos.get(player.getUniqueId()));
    }
}
