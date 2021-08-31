package tech.madsj;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class InviteCheckerRunnable implements Runnable {
    @Override
    public void run() {
        for (Invite inv : SpleefBattles.getInvites()) {
            if (inv.getTimeInvited() + Invite.getValidInviteTime() < System.currentTimeMillis()) {
                SpleefBattles.removeInvite(inv);
            }
        }
    }
}
