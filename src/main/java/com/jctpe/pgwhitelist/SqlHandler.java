package com.jctpe.pgwhitelist;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

public class SqlHandler {
//    FileConfiguration config = getConfig();
    private final FileConfiguration config;
    private final String ADDR;
    private final String PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final String DBNAME;
    private final String PATH;
    private final String DRIVERPATH;
    private final String DRIVER;
//    private final DriverManager DRIVER;

    public SqlHandler(FileConfiguration config) {
        this.config = config;
        this.ADDR = config.getString("sql-info.address");
        this.PORT = config.getString("sql-info.port");
        this.USERNAME = config.getString("sql-info.username");
        this.PASSWORD = config.getString("sql-info.password");
        this.DBNAME = config.getString("sql-info.db-name");
        this.PATH = "jdbc:postgresql://" + ADDR + ":" + PORT + "/" + DBNAME;
//        this.DRIVER = config.getString("SqlDriver");
//        this.DRIVERPATH = config.getString("SqlDriverJar");
        this.DRIVER = "org.postgresql.Driver";
        this.DRIVERPATH = "lib/postgresql-42.5.4.jar";
//        this.DRIVER = new DriverManager.registerDriver(); config.getString("sql-info.db-name");
    }

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
//Statement st = cnx.createStatement();
//            ResultSet rs = st.executeQuery("select * from \"PlayerList_playerdata\"");
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = """
                        SELECT * FROM whitelist
                        WHERE active
                        AND NOT ban
                        AND uuid = '""" + playerUuid.toString() + "'";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(stmt);
                int i = 0;
                while (rs.next()) {
                    i++;
                }
                cnx.close();
                return i == 1;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }

    // TODO: 還沒寫，只是先複製上面的下來作為 template
    public Boolean addPlayer(UUID playerUuid){
        try (Connection cnx = DriverManager.getConnection(PATH, USERNAME, PASSWORD)) {
            if (cnx != null) {
                String stmt = """
                        SELECT * FROM whitelist
                        WHERE active
                        AND NOT ban
                        AND uuid = '""" + playerUuid.toString() + "'";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(stmt);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        // TODO: 為了消紅線先返回 true，這部分邏輯仍需處理
        return true;
    }
}
