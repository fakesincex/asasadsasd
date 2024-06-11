package com.fakehardcore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

public class NoPvPZoneListener implements Listener {
    private final int minX = -10;
    private final int maxX = 10;
    private final int minY = 290;
    private final int maxY = 320;
    private final int minZ = -10;
    private final int maxZ = 10;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isInNoPvPZone(player)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isInNoPvPZone(Player player) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }
}
