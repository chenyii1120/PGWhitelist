package com.jctpe.pgwhitelist;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.getPGConfig;
import static com.jctpe.pgwhitelist.PGWhiteList.logger;

public class SqlHandler {
//    FileConfiguration config = getConfig();
    private final FileConfiguration config = getPGConfig();
    private final String ADDR = config.getString("sql-info.address");
    private final String PORT = config.getString("sql-info.port");
    private final String USERNAME = config.getString("sql-info.username");
    private final String PASSWORD = config.getString("sql-info.password");
    private final String DBNAME = config.getString("sql-info.db-name");
    private final String PATH = "jdbc:postgresql://" + ADDR + ":" + PORT + "/" + DBNAME;
    private final String DRIVERPATH = "org.postgresql.Driver";
    private final String DRIVER = "lib/postgresql-42.5.4.jar";
    private final String TABLE_NAME = "whitelist";
//    private final DriverManager DRIVER;

//    public SqlHandler() {
//        this.config = config;
//        this.ADDR = config.getString("sql-info.address");
//        this.PORT = config.getString("sql-info.port");
//        this.USERNAME = config.getString("sql-info.username");
//        this.PASSWORD = config.getString("sql-info.password");
//        this.DBNAME = config.getString("sql-info.db-name");
//        this.PATH = "jdbc:postgresql://" + ADDR + ":" + PORT + "/" + DBNAME;
////        this.DRIVER = config.getString("SqlDriver");
////        this.DRIVERPATH = config.getString("SqlDriverJar");
//        this.DRIVER = "org.postgresql.Driver";
//        this.DRIVERPATH = "lib/postgresql-42.5.4.jar";
//        this.DRIVER = new DriverManager.registerDriver(); config.getString("sql-info.db-name");
//    }

    public Boolean testConnection() {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                cnx.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public Boolean checkPlayer(UUID playerUuid) {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    SELECT * FROM %s
                    WHERE active
                    AND NOT ban
                    AND uuid = '%s'
                """, TABLE_NAME, playerUuid.toString());
                // 如果要用 ps 的話，statement 裡面要用 ? 代換參數，idx 從 1 起
                // PreparedStatement ps = cnx.prepareStatement(stmt);
                // ps.setString(1, playerUuid.toString());
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(stmt);
                int i = 0;
                while (rs.next()) {
                    i++;
                }
                boolean passFlag = i == 1;
                if (passFlag){
                    stmt = String.format("""
                        UPDATE %s SET
                            last_login = NOW()
                        WHERE uuid = '%s'
                    """, TABLE_NAME, playerUuid.toString());
                    st = cnx.createStatement();
                    try {
                        st.execute(stmt);
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public Boolean checkPlayerInDb(UUID playerUuid) {
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    SELECT * FROM %s
                    WHERE uuid = '%s'
                """, TABLE_NAME, playerUuid.toString());
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(stmt);
                return rs.next();
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
                        '%s', '%s', 't', 'f', NOW(), '%s'
                    )
                """, TABLE_NAME, playerUuid.toString(), targetID, senderID);
                Statement st = cnx.createStatement();
                try{
                    st.execute(stmt);
                } catch (SQLException e){
                    return "SQL insertion error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is added to the whitelist.", targetID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String deletePlayer(UUID playerUuid, String targetID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    DELETE FROM %s
                    WHERE uuid = '%s'
                """, TABLE_NAME, playerUuid.toString());
                Statement st = cnx.createStatement();
                try{
                    st.execute(stmt);
                } catch (SQLException e){
                    return "SQL deletion error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is deleted from the whitelist.", targetID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String banPlayer(UUID playerUuid, String targetID, String senderID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    UPDATE %s SET
                        ban = 't', update_time = NOW(), update_name = '%s'
                    WHERE uuid = '%s'
                """, TABLE_NAME, senderID, playerUuid.toString());
                Statement st = cnx.createStatement();
                try{
                    st.execute(stmt);
                } catch (SQLException e){
                    return "SQL update error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is now get banned.", targetID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }

    public String unbanPlayer(UUID playerUuid, String targetID, String senderID){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = String.format("""
                    UPDATE %s SET
                        ban = 'f', update_time = NOW(), update_name = '%s'
                    WHERE uuid = '%s'
                """, TABLE_NAME, senderID, playerUuid.toString());
                Statement st = cnx.createStatement();
                try{
                    st.execute(stmt);
                } catch (SQLException e){
                    return "SQL update error, please try again or check the params.";
                }
                return String.format("Player \"%s\" is no longer get banned.", targetID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "SQL connection error, please try again or check the params.";
        }

        return "SQL connection error, please try again or check the params.";
    }


}
