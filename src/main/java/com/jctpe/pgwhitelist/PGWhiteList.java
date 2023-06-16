package com.jctpe.pgwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public class PGWhiteList extends JavaPlugin {
    public static FileConfiguration config;
    public static Logger logger;
    private static PGWhiteList instance;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        logger.info("PG WhiteList now enabled!");
        saveDefaultConfig();
        config = getConfig();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SqlHandler dbh = new SqlHandler();
        boolean enabledFlag =  config.getBoolean("plugin-enable");
        if (enabledFlag) {
            logger.info("SQL Whitelisting now enabled.");
            if (dbh.testConnection()) {
//            if (true) {
                logger.info("SQL connected!");
            } else {
                logger.warning("Cannot connect to SQL, please check!");
            }
        } else {
            logger.info("SQL Whitelisting is disabled.");
        }
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("pgwhitelist")).setExecutor(new CommandHandler());
    }

    public static PGWhiteList getInstance(){ return instance; }

    public static FileConfiguration getPGConfig() {
        return config;
    }

    public boolean modifyAndSaveConfig(String key, Object newValue){
        if (!config.isSet(key)){
            logger.warning(String.format("This key %s is not in config file.", key));
            return false;
        } else {
            config.set(key, newValue);
            this.saveConfig();
            return true;
        }
    }

    private Logger getPGLogger() {
        return getLogger();
    }

    public static void logInfo(String _message) {
        logger.info(_message);
    }

    public static void pgKickPlayer(Player player, String _message) {
        player.kickPlayer(_message);
    }

}

