package com.jctpe.pgwhitelist;

import com.jctpe.pgwhitelist.commands.*;
import org.bukkit.Bukkit;
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
        Player tmpPlayer = null;

        if (cmd.equalsIgnoreCase("help")) {
            // return false 會被視為指令執行失敗，spigot 將會顯示指令說明訊息，因此在此處作為 help 內容顯示之用
            return false;
        }

        if (cmd.equalsIgnoreCase("list")) {
            sender.sendMessage("You can visit the web version whitelist: https://google.com/");
            return true;
        }

        // 這段用來處理如果玩家沒有 OP 權限，就 show message and early return
        if (senderIsPlayer){
            tmpPlayer = (Player) sender;
        }
        // 此部分以上可用來執行無需 OP 權限也可執行的指令
        if (tmpPlayer != null && !tmpPlayer.isOp()) {
            sender.sendMessage(String.format("[PGWhitelist] %s", "Sorry, you don't have permission to do that."));
            return true;
        }

        // 啟用白單
        if (cmd.equalsIgnoreCase("on")) {
            pluginSwitch.enablePlugin();
            sender.sendMessage("[PGWhitelist] Enabled!");
            return true;
        }

        // 停用白單
        if (cmd.equalsIgnoreCase("off")) {
            pluginSwitch.disablePlugin();
            sender.sendMessage("[PGWhitelist] Disabled!");
            return true;
        }

        // 將玩家加入白單
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
            String reason = "";
            if (args.length > 2){ reason = args[2]; }

            String rtn_msg = banPlayer.banPlayerById(args[1], reason, sender, senderIsPlayer);
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null && target.isOnline()){
                target.kickPlayer(String.format("Sorry, you just got banned because %s", reason));
            }
            sender.sendMessage(String.format("[PGWhitelist] %s", rtn_msg));
            return true;
        }

        if (cmd.equalsIgnoreCase("unban")) {
            String rtn_msg = unbanPlayer.unbanPlayerById(args[1], sender, senderIsPlayer);
            sender.sendMessage(String.format("[PGWhitelist] %s", rtn_msg));
            return true;
        }

        return false;
    }
}
