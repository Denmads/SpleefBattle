package tech.madsj;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpleefBattle {
    private Player owner;

    private List<Player> joinedPlayers;
    private SpleefArena arena;

    private BattleState state;

    public SpleefBattle(Player owner) {
        this.owner = owner;
        state = BattleState.IN_LOBBY;
        this.joinedPlayers = new ArrayList<>();
        joinedPlayers.add(owner);
    }

    public void join(Player player) {
        joinedPlayers.add(player);
    }

    public void leave(Player player) {
        joinedPlayers.remove(player);

        if (state == BattleState.STARTED) {
            arena.returnPlayer(player);
        }
    }

    public void start() {
        arena = new SpleefArena((int)Math.ceil(getNumPeopleJoined() / 2d));
        arena.create();
        for (Player player : joinedPlayers) {
            player.spigot().sendMessage(new ComponentBuilder("The battle will start in 5 seconds!").color(ChatColor.BLUE).bold(true).create());
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(SpleefBattlesPlugin.INSTANCE, (Runnable) () -> {
            for (Player player : joinedPlayers) {
                arena.teleportPlayer(player);
            }
        }, 20 * 5);
    }

    public void delete() {
        for (Player player : joinedPlayers) {
            player.removeMetadata(SpleefBattles.SPLEEF_BATTLE_OWNER_ID_KEY, SpleefBattlesPlugin.INSTANCE);
            player.spigot().sendMessage(new ComponentBuilder("The game you were part of, has been deleted!").color(ChatColor.BLUE).bold(true).create());
        }
    }

    public Player getOwner() {
        return owner;
    }

    public int getNumPeopleJoined() {
        return joinedPlayers.size();
    }

    public BattleState getState() {
        return state;
    }
}

enum BattleState {
    IN_LOBBY, STARTED
}
