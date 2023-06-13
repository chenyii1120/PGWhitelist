package com.jctpe.pgwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static com.jctpe.pgwhitelist.PGWhiteList.*;

public class EventListener implements Listener {

    public void whitelistCheck(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e){
        FileConfiguration config = getPGConfig();
        SqlHandler dbh = new SqlHandler(getPGConfig());
        UUID playerUuid = e.getPlayer().getUniqueId();
        if (!config.getBoolean("plugin-enable")){ return; }
        if (dbh.checkPlayer(playerUuid)) {
            logInfo(e.getPlayer().getDisplayName() + "is on whitelist");
        } else {
            logInfo(e.getPlayer().getDisplayName() + "is not on whitelist");
            String reason = "Sorry, you're not on whitelist. Please contact server admin for more info.";
//            pgKickPlayer(e.getPlayer(), reason);
//            String cmd = "/kick " + e.getPlayer().getDisplayName() + reason;
            e.getPlayer().kickPlayer("Sorry, you're not on whitelist. Please contact server admin for more info.");
//            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }
}
