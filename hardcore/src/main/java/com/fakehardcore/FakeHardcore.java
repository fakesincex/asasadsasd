package com.fakehardcore;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import com.fakehardcore.commands.*;
import com.fakehardcore.events.*;
import com.fakehardcore.utils.*;

public class FakeHardcore extends JavaPlugin {
    private TeamManager teamManager;
    private ConfigManager configManager;
    private GameTimer gameTimer;
    private BossBar bossBar;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();
        this.teamManager = new TeamManager(this);

        this.bossBar = getServer().createBossBar("Игра начинается...", BarColor.RED, BarStyle.SOLID);
        this.gameTimer = new GameTimer(this, bossBar);

        getCommand("hc").setExecutor(new CommandHandler(this));
        getCommand("hc").setTabCompleter(new CommandTabCompleter(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new NoPvPZoneListener(), this);

        getLogger().info("FakeHardcore был включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FakeHardcore был выключен!");
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
