package com.fakehardcore.utils;

import com.fakehardcore.FakeHardcore;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class TeamManager implements Listener {
    private FakeHardcore plugin;
    private Map<String, Team> teams;
    private Map<String, Integer> teamAliveCounts;
    private Map<Player, Boolean> teamHighlightStatus;

    public TeamManager(FakeHardcore plugin) {
        this.plugin = plugin;
        this.teams = new HashMap<>();
        this.teamAliveCounts = new HashMap<>();
        this.teamHighlightStatus = new HashMap<>();
        loadTeams();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void loadTeams() {
        if (plugin.getConfigManager().getTeamsConfig().getConfigurationSection("teams") != null) {
            for (String teamName : plugin.getConfigManager().getTeamsConfig().getConfigurationSection("teams").getKeys(false)) {
                ChatColor color = plugin.getConfigManager().getTeamColor(teamName);
                Team team = new Team(teamName, color);
                teams.put(teamName, team);
                Location loc = plugin.getConfigManager().getTeamLocation(teamName);
                team.setLocation(loc);
            }
        }

        if (plugin.getConfigManager().getPlayersConfig().getConfigurationSection("players") != null) {
            for (String playerName : plugin.getConfigManager().getPlayersConfig().getConfigurationSection("players").getKeys(false)) {
                String teamName = plugin.getConfigManager().getPlayerTeam(playerName);
                if (teamName != null) {
                    Player player = plugin.getServer().getPlayer(playerName);
                    if (player != null) {
                        addPlayerToTeam(teamName, player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String teamName = plugin.getConfigManager().getPlayerTeam(player.getName());
        if (teamName != null) {
            addPlayerToTeam(teamName, player);
        }

        if (plugin.getGameTimer().isGameRunning()) {
            plugin.getGameTimer().addPlayerToBossBar(player);
        }
    }

    public boolean createTeam(String name, ChatColor color) {
        if (teams.containsKey(name)) {
            return false;
        }
        teams.put(name, new Team(name, color));
        plugin.getConfigManager().saveTeamColor(name, color);
        return true;
    }

    public boolean deleteTeam(String name) {
        if (!teams.containsKey(name)) {
            return false;
        }
        teams.remove(name);
        plugin.getConfigManager().removeTeam(name);
        if (plugin.getConfigManager().getPlayersConfig().getConfigurationSection("players") != null) {
            for (String playerName : plugin.getConfigManager().getPlayersConfig().getConfigurationSection("players").getKeys(false)) {
                if (plugin.getConfigManager().getPlayerTeam(playerName).equals(name)) {
                    plugin.getConfigManager().removePlayerTeam(playerName);
                }
            }
        }
        return true;
    }

    public boolean addPlayerToTeam(String teamName, Player player) {
        Team team = teams.get(teamName);
        if (team == null) {
            return false;
        }
        team.addPlayer(player);
        player.setPlayerListName(team.getColor() + player.getName());
        plugin.getConfigManager().savePlayerTeam(player.getName(), teamName);
        return true;
    }

    public Set<String> getTeams() {
        return teams.keySet();
    }

    public Set<Player> getTeamPlayers(String teamName) {
        Team team = teams.get(teamName);
        return team != null ? team.getPlayers() : Collections.emptySet();
    }

    public Location getTeamLocation(String teamName) {
        Team team = teams.get(teamName);
        return team != null ? team.getLocation() : null;
    }

    public void setTeamLocation(String teamName, Location location) {
        Team team = teams.get(teamName);
        if (team != null) {
            team.setLocation(location);
            plugin.getConfigManager().saveTeamLocation(teamName, location);
        }
    }

    public void initializeTeamAliveCounts() {
        teamAliveCounts.clear();
        for (String teamName : getTeams()) {
            int count = getTeamPlayers(teamName).size();
            teamAliveCounts.put(teamName, count);
            plugin.getLogger().info("Команда " + teamName + " имеет " + count + " игроков.");
        }
    }

    public void decrementTeamAliveCount(String teamName) {
        if (teamAliveCounts.containsKey(teamName)) {
            int count = teamAliveCounts.get(teamName);
            teamAliveCounts.put(teamName, count - 1);
        }
    }

    public int getTeamAliveCount(String teamName) {
        return teamAliveCounts.getOrDefault(teamName, 0);
    }

    public void toggleHighlight(Player player) {
        boolean highlight = !teamHighlightStatus.getOrDefault(player, false);
        teamHighlightStatus.put(player, highlight);

        String teamName = plugin.getConfigManager().getPlayerTeam(player.getName());
        if (teamName != null) {
            for (Player p : getTeamPlayers(teamName)) {
                if (highlight) {
                    player.showPlayer(plugin, p);
                } else {
                    player.hidePlayer(plugin, p);
                }
            }
        }

        player.sendMessage("Подсветка для игроков вашей команды " + (highlight ? "включена" : "выключена"));
    }

    private class Team {
        private String name;
        private ChatColor color;
        private Set<Player> players;
        private Location location;

        public Team(String name, ChatColor color) {
            this.name = name;
            this.color = color;
            this.players = new HashSet<>();
        }

        public void addPlayer(Player player) {
            players.add(player);
        }

        public Set<Player> getPlayers() {
            return players;
        }

        public ChatColor getColor() {
            return color;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
}
