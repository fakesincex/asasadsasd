package com.fakehardcore.utils;

import com.fakehardcore.FakeHardcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class GameTimer {
    private FakeHardcore plugin;
    private BossBar bossBar;
    private int gameTime;
    private int borderTime;
    private int borderSize;
    private int initialBorderSize;
    private int taskId;
    private WorldBorder worldBorder;
    private boolean gameRunning;
    private Map<Player, Location> playerLocations;
    private final Location safeZoneLocation = new Location(Bukkit.getWorld("world"), 0, 292, 0);

    public GameTimer(FakeHardcore plugin, BossBar bossBar) {
        this.plugin = plugin;
        this.bossBar = bossBar;
        this.worldBorder = Bukkit.getWorlds().get(0).getWorldBorder();
        this.gameRunning = false;
        this.playerLocations = new HashMap<>();
        loadSettings();
    }

    public void loadSettings() {
        gameTime = plugin.getConfigManager().getConfig().getInt("gameTime", 3600);
        borderTime = plugin.getConfigManager().getConfig().getInt("borderTime", 900);
        borderSize = plugin.getConfigManager().getConfig().getInt("borderSize", 100);
        initialBorderSize = plugin.getConfigManager().getConfig().getInt("initialBorderSize", 1000);
    }

    public void startGame() {
        if (gameRunning) {
            return;
        }
        gameRunning = true;

        worldBorder.setSize(initialBorderSize);

        bossBar.setVisible(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayerToBossBar(player);
            playerLocations.put(player, player.getLocation());
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
            player.setFoodLevel(20);
            player.setSaturation(20.0f);
        }

        plugin.getTeamManager().initializeTeamAliveCounts();

        teleportPlayersToTeamLocations();

        taskId = new BukkitRunnable() {
            int timeLeft = gameTime;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    bossBar.setTitle("Время до сужения границы: " + minutes + " мин " + seconds + " сек");
                    bossBar.setProgress((double) timeLeft / gameTime);
                    timeLeft--;
                } else {
                    bossBar.setTitle("Граница мира сужается!");
                    bossBar.setProgress(0);
                    startBorderShrink();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20).getTaskId();
    }

    public void startBorderShrink() {
        worldBorder.setSize(borderSize, borderTime);
    }

    public void cancelGame() {
        if (!gameRunning) {
            return;
        }
        Bukkit.getScheduler().cancelTask(taskId);
        bossBar.setVisible(false);
        worldBorder.reset();
        teleportPlayersToSafeZone();
        gameRunning = false;
    }

    public void endGame(String winningTeam) {
        if (!gameRunning) {
            return;
        }
        Bukkit.getScheduler().cancelTask(taskId);
        bossBar.setVisible(false);
        worldBorder.reset();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        teleportPlayersToSafeZone();
        gameRunning = false;
        plugin.getServer().broadcastMessage("Игра окончена! Победила команда " + winningTeam);
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void addPlayerToBossBar(Player player) {
        bossBar.addPlayer(player);
    }

    private void teleportPlayersToTeamLocations() {
        for (String team : plugin.getTeamManager().getTeams()) {
            Location loc = plugin.getTeamManager().getTeamLocation(team);
            if (loc != null) {
                for (Player player : plugin.getTeamManager().getTeamPlayers(team)) {
                    player.teleport(loc);
                }
            }
        }
    }

    private void teleportPlayersToSafeZone() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(safeZoneLocation);
        }
    }

    private void teleportPlayersBack() {
        for (Map.Entry<Player, Location> entry : playerLocations.entrySet()) {
            entry.getKey().teleport(entry.getValue());
        }
        playerLocations.clear();
    }
}
