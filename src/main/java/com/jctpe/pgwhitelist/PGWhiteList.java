package com.jctpe.pgwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

public class PGWhiteList extends JavaPlugin {
    public static FileConfiguration config;
    public static Logger logger;
    private static PGWhiteList instance;
//    private static CommandHandler commandHandler = new CommandHandler();
//    private static SqlHandler dbh;

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
        SqlHandler dbh = new SqlHandler(config);
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
        // 注册事件处理器，这里必须实例化，this 表明注册到本插件上
         Objects.requireNonNull(Bukkit.getPluginCommand("pgwhitelist")).setExecutor(new CommandHandler());
        // 注册事件处理器，也要实例化，requireNonNull 是不必要的，但是万一插件损坏了或者 Bukkit 出错了，我们还能知道是这里出问题
        // instance = this;
        // 小技巧：暴露实例
    }

//    public static SqlHandler getSqlHandler() {
//        return dbh;
//    }
    public static PGWhiteList getInstance(){ return instance; }

    public static FileConfiguration getPGConfig() {
        return config;
    }

//    public void savePGConfig(){ this.saveConfig(); }

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
//        Logger logger = getPGLogger();
        logger.info(_message);
    }

    public static void pgKickPlayer(Player player, String _message) {
        player.kickPlayer(_message);
    }

}

