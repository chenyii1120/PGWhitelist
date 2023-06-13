package com.jctpe.pgwhitelist.commands;

import com.jctpe.pgwhitelist.PGWhiteList;
import org.bukkit.configuration.file.FileConfiguration;

import static com.jctpe.pgwhitelist.PGWhiteList.getInstance;
import static com.jctpe.pgwhitelist.PGWhiteList.getPGConfig;

public class pluginSwitch {

    public static void enablePlugin() {
        FileConfiguration config = getPGConfig();
//        config.set("enabled-sql", false);
        PGWhiteList.getInstance().modifyAndSaveConfig("plugin-enable", true);

    }

    public static void disablePlugin() {
        FileConfiguration config = getPGConfig();
//        config.set("enabled-sql", false);
        PGWhiteList.getInstance().modifyAndSaveConfig("plugin-enable", false);

    }
}
