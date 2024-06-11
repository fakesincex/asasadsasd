package com.fakehardcore.events;

import com.fakehardcore.FakeHardcore;
import com.fakehardcore.utils.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerDeathListener implements Listener {
    private FakeHardcore plugin;

    public PlayerDeathListener(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Проверяем, запущена ли игра
        if (!plugin.getGameTimer().isGameRunning()) {
            return;
        }

        Player player = event.getEntity();
        player.setGameMode(GameMode.SPECTATOR);

        String teamName = plugin.getConfigManager().getPlayerTeam(player.getName());
        String deathMessage = event.getDeathMessage();
        TeamManager teamManager = plugin.getTeamManager();

        teamManager.decrementTeamAliveCount(teamName);
        int aliveCount = teamManager.getTeamAliveCount(teamName);

        String message = ChatColor.RED + "Team " + teamName + ": " + player.getName() + " умер. Осталось живых игроков этой команды: " + aliveCount;
        plugin.getServer().broadcastMessage(message);

        // Проверка на победу
        checkForWinCondition();
    }

    private void checkForWinCondition() {
        TeamManager teamManager = plugin.getTeamManager();
        String winningTeam = null;
        boolean isAnyTeamAlive = false;

        for (String teamName : teamManager.getTeams()) {
            int aliveCount = teamManager.getTeamAliveCount(teamName);

            if (aliveCount > 0) {
                if (winningTeam == null) {
                    winningTeam = teamName;
                } else {
                    isAnyTeamAlive = true;
                    break; // Больше одной команды с живыми игроками
                }
            }
        }

        if (!isAnyTeamAlive && winningTeam != null) {
            plugin.getServer().broadcastMessage(ChatColor.GREEN + "Победила команда - " + winningTeam);
            plugin.getGameTimer().endGame(winningTeam);
        }
    }
}
