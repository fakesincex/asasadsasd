package com.fakehardcore.utils;

import com.fakehardcore.FakeHardcore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Location;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private FakeHardcore plugin;
    private File configFile;
    private FileConfiguration config;
    private File playersFile;
    private FileConfiguration playersConfig;
    private File teamsFile;
    private FileConfiguration teamsConfig;

    public ConfigManager(FakeHardcore plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "settings.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.playersFile = new File(plugin.getDataFolder(), "players.yml");
        this.playersConfig = YamlConfiguration.loadConfiguration(playersFile);
        this.teamsFile = new File(plugin.getDataFolder(), "teams.yml");
        this.teamsConfig = YamlConfiguration.loadConfiguration(teamsFile);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!playersFile.exists()) {
            plugin.saveResource("players.yml", false);
        }
        playersConfig = YamlConfiguration.loadConfiguration(playersFile);
        if (!teamsFile.exists()) {
            plugin.saveResource("teams.yml", false);
        }
        teamsConfig = YamlConfiguration.loadConfiguration(teamsFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getPlayersConfig() {
        return playersConfig;
    }

    public FileConfiguration getTeamsConfig() {
        return teamsConfig;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить конфигурацию: " + e.getMessage());
        }
    }

    public void savePlayersConfig() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить players.yml: " + e.getMessage());
        }
    }

    public void saveTeamsConfig() {
        try {
            teamsConfig.save(teamsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить teams.yml: " + e.getMessage());
        }
    }

    public void saveTeamLocation(String teamName, Location location) {
        String path = "teams." + teamName + ".location";
        teamsConfig.set(path + ".world", location.getWorld().getName());
        teamsConfig.set(path + ".x", location.getX());
        teamsConfig.set(path + ".y", location.getY());
        teamsConfig.set(path + ".z", location.getZ());
        saveTeamsConfig();
    }

    public Location getTeamLocation(String teamName) {
        String path = "teams." + teamName + ".location";
        String worldName = teamsConfig.getString(path + ".world");
        if (worldName == null) return null;
        double x = teamsConfig.getDouble(path + ".x");
        double y = teamsConfig.getDouble(path + ".y");
        double z = teamsConfig.getDouble(path + ".z");
        return new Location(plugin.getServer().getWorld(worldName), x, y, z);
    }

    public void savePlayerTeam(String playerName, String teamName) {
        playersConfig.set("players." + playerName + ".team", teamName);
        savePlayersConfig();
    }

    public String getPlayerTeam(String playerName) {
        return playersConfig.getString("players." + playerName + ".team");
    }

    public void removePlayerTeam(String playerName) {
        playersConfig.set("players." + playerName, null);
        savePlayersConfig();
    }

    public void removeTeam(String teamName) {
        teamsConfig.set("teams." + teamName, null);
        saveTeamsConfig();
    }

    public void saveTeamColor(String teamName, ChatColor color) {
        teamsConfig.set("teams." + teamName + ".color", color.name());
        saveTeamsConfig();
    }

    public ChatColor getTeamColor(String teamName) {
        String colorName = teamsConfig.getString("teams." + teamName + ".color");
        return colorName != null ? ChatColor.valueOf(colorName) : ChatColor.WHITE;
    }
}
