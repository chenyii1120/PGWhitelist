package com.jctpe.pgwhitelist.commands;

import com.jctpe.pgwhitelist.SqlHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.logInfo;

public class addPlayer {

    public static boolean addPlayerToWhitelist(String userID, CommandSender sender, boolean sendByPlayer){
        SqlHandler dbh = new SqlHandler();
        UUID uuid;
        try {
            uuid = mojangAPI.checkPlayerUUID(userID);
        } catch (Exception e) {
            logInfo("Some error occur");
            e.printStackTrace();
            return false;
        }

        if (!dbh.checkPlayerInDb(uuid)){ return false; }

        String senderID = "SYSTEM";
        if (sendByPlayer){
            Player player = (Player) sender;
            senderID = player.getDisplayName();
        }

        return dbh.addPlayer(uuid, userID, senderID);
    }
}
