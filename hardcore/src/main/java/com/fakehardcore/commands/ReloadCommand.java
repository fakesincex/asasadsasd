package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ReloadCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public ReloadCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getConfigManager().loadConfig();
        plugin.getGameTimer().loadSettings();
        sender.sendMessage("Конфигурационные файлы перезагружены.");
        return true;
    }
}
