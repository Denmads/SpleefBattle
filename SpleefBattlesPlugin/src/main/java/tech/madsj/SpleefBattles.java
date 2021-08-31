package tech.madsj;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class SpleefBattles {
    private static Map<UUID, SpleefBattle> ownerBattleMap = new HashMap<>();
    private static Map<UUID, Invite> invites = new HashMap<>();


    public static String SPLEEF_BATTLE_OWNER_ID_KEY = "spleef-battle-owner-id";

    private static MetadataValue getJoinedMetaData(Player player) {
        List<MetadataValue> values = player.getMetadata(SPLEEF_BATTLE_OWNER_ID_KEY);
        MetadataValue ownerId = null;
        for (MetadataValue val : values) {
            if (val.getOwningPlugin() == SpleefBattlesPlugin.INSTANCE) {
                ownerId = val;
                break;
            }
        }
        return ownerId;
    }

    public static SpleefBattle getBattle(UUID owner) {
        return ownerBattleMap.get(owner);
    }

    public static List<Invite> getInvites() {
        return new ArrayList<>(invites.values());
    }

    public static void removeInvite(Invite invite) {
        invites.remove(invite.getInvited().getUniqueId());
    }

    public static void removeInvite(Player player) {
        invites.remove(player.getUniqueId());
    }

    public static void acceptInvite(Player player) {
        Invite invite = invites.get(player.getUniqueId());
        if (invite == null) {
            player.spigot().sendMessage(new ComponentBuilder("You don't have a pending invite!").color(ChatColor.RED).create());
            return;
        }

        invites.remove(player.getUniqueId());
        join(invite.getOwner(), player);
    }

    public static SpleefBattle createNew(Player owner) {
        SpleefBattle battle = new SpleefBattle(owner);
        ownerBattleMap.put(owner.getUniqueId(), battle);
        owner.setMetadata(SPLEEF_BATTLE_OWNER_ID_KEY, new FixedMetadataValue(SpleefBattlesPlugin.INSTANCE, owner.getUniqueId().toString()));

        owner.spigot().sendMessage(new ComponentBuilder("Battle created!").color(ChatColor.GREEN).create());
        return battle;
    }

    public static void delete(Player owner) {
        SpleefBattle battle = getBattle(owner.getUniqueId());
        if (battle == null) {
            owner.spigot().sendMessage(new ComponentBuilder("You don't have a created game!").color(ChatColor.RED).create());
            return;
        }

        ownerBattleMap.remove(owner.getUniqueId());
        battle.delete();
    }

    public static void join(Player owner, Player player) {
        MetadataValue ownerData = getJoinedMetaData(player);

        if (ownerData != null) {
            UUID oldOwner = UUID.fromString(ownerData.asString());
            SpleefBattle battle = getBattle(oldOwner);

            if (battle != null) {
                player.spigot().sendMessage(new ComponentBuilder("You are already in another battle!").color(ChatColor.RED).create());
                return;
            }
            else {
                player.removeMetadata(SPLEEF_BATTLE_OWNER_ID_KEY, SpleefBattlesPlugin.INSTANCE);
            }
        }

        SpleefBattle battle = getBattle(owner.getUniqueId());
        if (battle == null) {
            player.spigot().sendMessage(new ComponentBuilder("The battle you tried to join does not exists anymore!").color(ChatColor.RED).create());
            return;
        }

        battle.join(player);
        player.setMetadata(SPLEEF_BATTLE_OWNER_ID_KEY, new FixedMetadataValue(SpleefBattlesPlugin.INSTANCE, owner.getUniqueId().toString()));
        player.spigot().sendMessage(new ComponentBuilder("Joined the battle!").color(ChatColor.GREEN).create());
    }

    public static void leave(Player player) {
        MetadataValue ownerData = getJoinedMetaData(player);

        if (ownerData == null) {
            player.spigot().sendMessage(new ComponentBuilder("You are not currently joined any battle!").color(ChatColor.RED).create());
            return;
        }

        player.removeMetadata(SPLEEF_BATTLE_OWNER_ID_KEY, SpleefBattlesPlugin.INSTANCE);

        UUID owner = UUID.fromString(ownerData.asString());
        SpleefBattle battle = getBattle(owner);
        if (battle == null) {
            player.spigot().sendMessage(new ComponentBuilder("The battle you tried to leave do no longer exist!").color(ChatColor.RED).create());
            return;
        }

        battle.leave(player);
        player.spigot().sendMessage(new ComponentBuilder("Left the battle!").color(ChatColor.GREEN).create());
    }

    public static void invite(Player owner, Player player) {
        if (invites.get(player.getUniqueId()) != null) {
            owner.spigot().sendMessage(new ComponentBuilder(player.getDisplayName() + " is already in a battle!").color(ChatColor.RED).create());
            return;
        }

        Invite invite = new Invite(owner);
        invites.put(player.getUniqueId(), invite);
        invite.execute(player);
        owner.spigot().sendMessage(new ComponentBuilder(player.getDisplayName() + " successfully invited!").color(ChatColor.GREEN).create());
    }
}
