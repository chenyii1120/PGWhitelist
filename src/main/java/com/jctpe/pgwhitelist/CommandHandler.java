package com.jctpe.pgwhitelist;

import com.jctpe.pgwhitelist.commands.pluginSwitch;
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
            // 執行你的命令邏輯
            pluginSwitch.enablePlugin();
            sender.sendMessage("[PGWhitelist] Enabled!");

            // 在此處添加你的其他命令處理邏輯
            return true;
        }

        if (cmd.equalsIgnoreCase("off")) {
            // 執行你的命令邏輯
            pluginSwitch.disablePlugin();
            sender.sendMessage("[PGWhitelist] Disabled!");

            // 在此處添加你的其他命令處理邏輯
            return true;
        }

        if (cmd.equalsIgnoreCase("add")) {
            // 執行你的命令邏輯
            pluginSwitch.disablePlugin();
            sender.sendMessage("[PGWhitelist] Disabled!");

            // 在此處添加你的其他命令處理邏輯
            return true;
        }

        return false;
    }
}
