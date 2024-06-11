package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class CancelCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public CancelCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (!plugin.getGameTimer().isGameRunning()) {
            sender.sendMessage("Игра не запущена.");
            return true;
        }

        plugin.getGameTimer().cancelGame();
        sender.sendMessage("Игра отменена!");
        return true;
    }
}
