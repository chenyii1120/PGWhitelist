package com.jctpe.pgwhitelist.commands;

import com.jctpe.pgwhitelist.SqlHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.logInfo;

public class unbanPlayer {

    public static String unbanPlayerById(String userID, CommandSender sender, boolean sendByPlayer){
        SqlHandler dbh = new SqlHandler();
        UUID uuid;
        try {
            uuid = mojangAPI.checkPlayerUUID(userID);
        } catch (Exception e) {
            logInfo("Some error occur");
            e.printStackTrace();
            return "Mojang API connection failed, please try again. If this situation continues, please check your Internet connection.";
        }

        if (!dbh.checkPlayerInDb(uuid)){ return String.format("This player \"%s\" is not in database.", userID); }

        String senderID = "SYSTEM";
        if (sendByPlayer){
            Player player = (Player) sender;
            senderID = player.getDisplayName();
        }

        return dbh.unbanPlayer(uuid, userID, senderID);
    }
}
