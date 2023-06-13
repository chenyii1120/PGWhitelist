package com.jctpe.pgwhitelist.commands;

import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.logInfo;

public class addPlayer {
    public boolean addPlayerToWhitelist(String userID) throws Exception{
        try {
            UUID uuid = mojangAPI.checkPlayerUUID(userID);
        } catch (Exception e) {
            logInfo("Some error occur");
            e.printStackTrace();
//            logInfo(e.toString());
            return false;
        }

        // TODO: 後續還沒寫完，只是為了避免紅線所以先返回一個 true
        return true;

    }
}
