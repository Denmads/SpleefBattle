package tech.madsj;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

public class Invite {
    private static int VALID_INVITE_TIME = 1000 * 60; //60 seconds

    private Player owner;
    private long timeInvited;
    private Player invited;

    public Invite(Player owner) {
        this.owner = owner;
    }

    public void execute(Player invited) {
        this.timeInvited = System.currentTimeMillis();
        this.invited = invited;
        invited.spigot().sendMessage(new ComponentBuilder("You have been invited by ")
                                        .append(owner.getDisplayName()).color(ChatColor.GOLD)
                                        .append(" to a spleef battle.\n")
                                        .append("Accept").color(ChatColor.GREEN).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "spleef-accept-invite"))
                                        .append(" / ")
                                        .append("Deny").color(ChatColor.RED).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "spleef-deny-invite")).create());
    }

    public static int getValidInviteTime() {
        return VALID_INVITE_TIME;
    }

    public Player getOwner() {
        return owner;
    }

    public long getTimeInvited() {
        return timeInvited;
    }

    public Player getInvited() {
        return invited;
    }
}
