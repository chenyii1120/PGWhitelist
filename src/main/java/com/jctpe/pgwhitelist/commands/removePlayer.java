package com.jctpe.pgwhitelist.commands;

import com.jctpe.pgwhitelist.SqlHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.logInfo;

public class removePlayer {

    public static String removePlayerFromWhitelist(String userID){
        SqlHandler dbh = new SqlHandler();
        UUID uuid;
        try {
            uuid = mojangAPI.checkPlayerUUID(userID);
        } catch (Exception e) {
            logInfo("Mojang API connection failed.");
            e.printStackTrace();
//            return "Mojang API connection failed, please try again. If this situation continues, please check your Internet connection.";
            return "Mojang API 連線失敗, 請再試一次。如果這個情況持續發生，請檢查伺服器的網路連線。";
        }

        if (!dbh.checkPlayerInDb(uuid)){ return String.format("該玩家 \"%s\" 不存在於資料庫內.", userID); }

        return dbh.deletePlayer(uuid, userID);
    }

}
