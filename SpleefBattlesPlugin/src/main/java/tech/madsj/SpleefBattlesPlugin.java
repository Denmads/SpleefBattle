package tech.madsj;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class SpleefBattlesPlugin extends JavaPlugin {
    public static SpleefBattlesPlugin INSTANCE;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        SpleefBattlesPlugin.INSTANCE = this;

        createSpleefWorld();

        getCommand("spleef").setExecutor(new SpleefCommand());
        getCommand("spleef-accept-invite").setExecutor(new AcceptInviteCommand());
        getCommand("spleef-deny-invite").setExecutor(new DenyInviteCommand());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new InviteCheckerRunnable(), 0, 20 * 30);
    }

    private void createSpleefWorld () {
        WorldCreator creator = new WorldCreator("spleef");
        creator.generator(new ChunkGenerator() {
            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                return createChunkData(world);
            }
        });
        creator.createWorld();
    }
}
