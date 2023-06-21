package com.jctpe.pgwhitelist;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.getPGConfig;

public class SqlHandler {
    private final FileConfiguration config = getPGConfig();
    private final String ADDR = config.getString("sql-info.address");
    private final String PORT = config.getString("sql-info.port");
    private final String USERNAME = config.getString("sql-info.username");
    private final String PASSWORD = config.getString("sql-info.password");
    private final String DBNAME = config.getString("sql-info.db-name");
    private final String PATH = "jdbc:postgresql://" + ADDR + ":" + PORT + "/" + DBNAME;
    private final String DRIVERPATH = "org.postgresql.Driver";
    private final String DRIVER = "lib/postgresql-42.5.4.jar";
    private final String TABLE_NAME = "pg_whitelist";

    public Boolean testConnection() {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                cnx.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String checkPlayer(UUID playerUuid) {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
//                String stmt = String.format("""
//                    SELECT * FROM %s
//                    WHERE active
//                    AND NOT ban
//                    AND uuid = ?
//                """, TABLE_NAME);
                String stmt = String.format("""
                    SELECT * FROM %s
                    WHERE uuid = ?
                """, TABLE_NAME);
                // 如果要用 ps 的話，statement 裡面要用 ? 代換參數，idx 從 1 起
                PreparedStatement ps = cnx.prepareStatement(stmt);
                ps.setObject(1, playerUuid);
                ResultSet rs = ps.executeQuery();
                int i = 0;
                boolean active = false;
                boolean ban = true;
                String deactivateDesc = "";
                String banDesc = "";
                while (rs.next()) {
                    i++;
                    active = rs.getBoolean("is_active");
                    ban = rs.getBoolean("is_banned");
                    deactivateDesc = rs.getString("deactivate_desc");
                    banDesc = rs.getString("ban_desc");
                }
                rs.close();
                boolean passFlag = i == 1;
                if (passFlag){
                    if (!active){
                        if (deactivateDesc == null){
                            deactivateDesc = "帳號已暫時被停用，請嘗試至 https://cnf.rn-ws.com/ 刷新你的 Discord 驗證權杖。";
                        }
                        return deactivateDesc;
                    }
                    if (ban){
                        return banDesc;
                    }
                    stmt = String.format("""
                        UPDATE %s SET
                            last_login = NOW()
                        WHERE uuid = ?
                    """, TABLE_NAME);
                    ps = cnx.prepareStatement(stmt);
                    ps.setObject(1, playerUuid);
                    try {
                        ps.execute();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    ps.close();
                    return "OK";
                }
            }
            return "抱歉，你不在白名單上。";
        } catch (SQLException e) {
            e.printStackTrace();
            return "false";
        }
    }

    public Boolean checkPlayerInDb(UUID playerUuid) {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    SELECT * FROM %s
                    WHERE uuid = ?
                """, TABLE_NAME);
                PreparedStatement ps = cnx.prepareStatement(stmt);
                ps.setObject(1, playerUuid);
                ResultSet rs = ps.executeQuery();
                boolean flag = rs.next();
                ps.close();
                rs.close();
                return flag;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String addPlayer(UUID playerUuid, String targetID, String senderID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    INSERT INTO %s (
                        uuid, player_id, active, ban, input_time, input_name
                    ) VALUES (
                        ?, ?, 't', 'f', NOW(), ?
                    )
                """, TABLE_NAME);
                PreparedStatement ps = cnx.prepareStatement(stmt);
                try (ps) {
                    ps.setObject(1, playerUuid);
                    ps.setString(2, targetID);
                    ps.setString(3, senderID);
                    ps.execute();
                } catch (SQLException e) {
                    return "SQL insertion error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is added to the whitelist.", targetID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String deletePlayer(UUID playerUuid, String targetID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    DELETE FROM %s
                    WHERE uuid = ?
                """, TABLE_NAME);
                PreparedStatement ps = cnx.prepareStatement(stmt);
                try(ps){
                    ps.setObject(1, playerUuid);
                    ps.execute();
                } catch (SQLException e){
                    return "SQL deletion error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is deleted from the whitelist.", targetID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String banPlayer(UUID playerUuid, String bannedReason, String targetID, String senderID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    UPDATE %s SET
                        ban = 't', update_time = NOW(), update_name = ?, ban_desc = ?
                    WHERE uuid = ?
                """, TABLE_NAME);
                PreparedStatement ps = cnx.prepareStatement(stmt);
                try (ps){
                    ps.setString(1, senderID);
                    ps.setString(2, bannedReason);
                    ps.setObject(3, playerUuid);
                    ps.execute();
                } catch (SQLException e){
                    return "SQL update error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is now get banned.", targetID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String unbanPlayer(UUID playerUuid, String targetID, String senderID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    UPDATE %s SET
                        ban = 'f', update_time = NOW(), update_name = ?
                    WHERE uuid = ?
                """, TABLE_NAME);
                PreparedStatement ps = cnx.prepareStatement(stmt);
                try (ps){
                    ps.setString(1, senderID);
                    ps.setObject(2, playerUuid);
                    ps.execute();
                } catch (SQLException e){
                    return "SQL update error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is no longer get banned.", targetID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }


}
