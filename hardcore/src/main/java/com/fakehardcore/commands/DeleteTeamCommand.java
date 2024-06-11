package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import com.fakehardcore.utils.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class DeleteTeamCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public DeleteTeamCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Использование: /hc delete <team>");
            return false;
        }

        String teamName = args[1];
        TeamManager teamManager = plugin.getTeamManager();

        if (teamManager.deleteTeam(teamName)) {
            sender.sendMessage("Команда " + teamName + " удалена.");
        } else {
            sender.sendMessage("Не удалось удалить команду " + teamName + ".");
        }

        return true;
    }
}
