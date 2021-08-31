package tech.madsj;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class SpleefCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length > 0) {
            Player player = (Player)sender;

            if (args[0].toLowerCase().equals("help") && args.length == 1) {
                printHelp(sender);
                return true;
            }
            else if (args[0].toLowerCase().equals("create") && args.length == 1) {
                SpleefBattles.createNew(player);
                return true;
            }
            else if (args[0].toLowerCase().equals("start") && args.length == 1) {
                SpleefBattle battle = SpleefBattles.getBattle(player.getUniqueId());
                if (battle == null) {
                    player.spigot().sendMessage(new ComponentBuilder("You don't have a created battle!").color(ChatColor.RED).create());
                    return true;
                }
                else if (battle.getNumPeopleJoined() < 2) {
                    player.spigot().sendMessage(new ComponentBuilder("Not enough people to start the battle!").color(ChatColor.RED).create());
                    return true;
                }

                battle.start();
                return true;
            }
            else if (args[0].toLowerCase().equals("invite") && args.length == 2) {
                Player invited = Bukkit.getPlayer(args[1]);
                if (invited == null) {
                    player.spigot().sendMessage(new ComponentBuilder("No player could be found for '" + args[1] + "'!").color(ChatColor.RED).create());
                    return true;
                }

                SpleefBattles.invite(player, invited);
                return true;
            }
            else if (args[0].toLowerCase().equals("info") && args.length == 1) {
                List<MetadataValue> values = player.getMetadata(SpleefBattles.SPLEEF_BATTLE_OWNER_ID_KEY);
                MetadataValue ownerVal = null;
                for (MetadataValue val : values) {
                    if (val.getOwningPlugin() == SpleefBattlesPlugin.INSTANCE) {
                        ownerVal = val;
                        break;
                    }
                }

                if (ownerVal == null) {
                    player.spigot().sendMessage(new ComponentBuilder("You have not joined any battle!").color(ChatColor.RED).create());
                    return true;
                }

                UUID ownerId = UUID.fromString(ownerVal.asString());
                SpleefBattle battle = SpleefBattles.getBattle(ownerId);

                player.spigot().sendMessage(new ComponentBuilder("Battle Info:\n")
                        .append("Owner: " + battle.getOwner().getDisplayName() + "\n")
                        .append("Number of people joined: " + battle.getNumPeopleJoined()).create());
                return true;
            }
            else if (args[0].toLowerCase().equals("delete") && args.length == 1) {
                SpleefBattles.delete(player);
                return true;
            }
            else if (args[0].toLowerCase().equals("leave") && args.length == 1) {
                SpleefBattles.leave(player);
                return true;
            }
        }

        return false;
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage(new String[]{
                "Usage: /spleef <option> <extra>",
                "create - Creates a new spleef battle",
                "start - Starts the spleef battle (At least two players needed).",
                "invite <PlayerName> - Send invite to the specified player ('create' must be called first')",
                "info - See info for the created or joined spleef battle",
                "delete - Deletes the created battle",
                "leave - Leaves the joined battle"
        });
    }
}
