package com.jctpe.pgwhitelist.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.json.JSONObject;

public class mojangAPI {

    public static UUID checkPlayerUUID(String userID) throws Exception {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + userID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String res_str = response.toString();

        // 使用org.json.JSONObject解析JSON數據
        JSONObject jsonObject = new JSONObject(res_str);

        // 獲取UUID
        String uuidString = jsonObject.getString("id");

        String uuidStr = String.format("%s-%s-%s-%s-%s", uuidString.substring(0,8), uuidString.substring(8,12),
                uuidString.substring(12,16), uuidString.substring(16,20), uuidString.substring(20));

        return UUID.fromString(uuidStr);
    }
}
