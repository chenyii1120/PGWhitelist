package com.jctpe.pgwhitelist;

import com.jctpe.pgwhitelist.commands.addPlayer;
import com.jctpe.pgwhitelist.commands.banPlayer;
import com.jctpe.pgwhitelist.commands.pluginSwitch;
import com.jctpe.pgwhitelist.commands.removePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class CommandHandler implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean senderIsPlayer = sender instanceof Player;
        String cmd = args[0];

        if (cmd.equalsIgnoreCase("on")) {
            pluginSwitch.enablePlugin();
            sender.sendMessage("[PGWhitelist] Enabled!");
            return true;
        }

        if (cmd.equalsIgnoreCase("off")) {
            pluginSwitch.disablePlugin();
            sender.sendMessage("[PGWhitelist] Disabled!");
            return true;
        }

        if (cmd.equalsIgnoreCase("add")) {
            String rtn_msg = addPlayer.addPlayerToWhitelist(args[1], sender, senderIsPlayer);
            sender.sendMessage(String.format("[PGWhitelist] %s", rtn_msg));
            return true;
        }

        if (cmd.equalsIgnoreCase("remove")) {
            String rtn_msg = removePlayer.removePlayerFromWhitelist(args[1]);
            sender.sendMessage(String.format("[PGWhitelist] %s", rtn_msg));
            return true;
        }

        if (cmd.equalsIgnoreCase("ban")) {
            String rtn_msg = banPlayer.banPlayerById(args[1], sender, senderIsPlayer);
            sender.sendMessage(String.format("[PGWhitelist] %s", rtn_msg));
            return true;
        }

        return false;
    }
}
