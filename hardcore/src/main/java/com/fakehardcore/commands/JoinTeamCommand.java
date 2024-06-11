package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class JoinTeamCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public JoinTeamCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (plugin.getGameTimer().isGameRunning()) {
            sender.sendMessage("Невозможно присоединиться к команде во время игры.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Использование: /hc join <team>");
            return false;
        }

        String teamName = args[1];
        Player player = (Player) sender;

        if (plugin.getTeamManager().addPlayerToTeam(teamName, player)) {
            sender.sendMessage("Вы присоединились к команде " + teamName);
        } else {
            sender.sendMessage("Не удалось присоединиться к команде " + teamName);
        }

        return true;
    }
}
