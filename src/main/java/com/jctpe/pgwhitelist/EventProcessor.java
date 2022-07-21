package com.jctpe.pgwhitelist;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventProcessor implements Listener {

    public void whitelistCheck(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();
    }
}
