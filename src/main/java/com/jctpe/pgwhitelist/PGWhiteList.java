package com.jctpe.pgwhitelist;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PGWhiteList extends JavaPlugin {

//    private FileConfiguration config = this.getConfig();
    @Override
    public void onEnable() {
        getLogger().info("PG WhiteList now enabled!");
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SqlHandler dbh = new SqlHandler(config);
        boolean sqlEnabled =  config.getBoolean("enabled-sql");
        if (sqlEnabled) {
            getLogger().info("SQL Whitelisting now enabled.");
            if (dbh.testConnection()) {
//            if (true) {
                getLogger().info("SQL connected!");
            } else {
                getLogger().warning("Cannot connect to SQL, please check!");
            }
        } else {
            getLogger().info("SQL Whitelisting is disabled.");
        }
    }
}

