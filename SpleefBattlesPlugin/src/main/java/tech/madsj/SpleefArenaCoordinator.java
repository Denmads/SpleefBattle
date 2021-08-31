package tech.madsj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public abstract class SpleefArenaCoordinator {

    private static List<Location> takenLocations = new ArrayList<>();
    private static World world = null;

    public static Location getArenaLocation() {

        if (world == null) {
            world = Bukkit.getWorld("spleef");
        }

        Location loc = null;

        do {
            int x = (int)Math.floor(Math.random() * 1001 - 500);
            int z = (int)Math.floor(Math.random() * 1001 - 500);

            loc = new Location(world, x * 500, 0, z * 500);
        } while (!takenLocations.contains(loc));

        takenLocations.add(loc);
        return loc;
    }

    public static void releaseLocation(Location location) {
        takenLocations.remove(location);
    }
}
