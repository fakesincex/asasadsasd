package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import com.fakehardcore.utils.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class AddPlayerToTeamCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public AddPlayerToTeamCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Usage: /hc team <team> <nickname>");
            return false;
        }

        String teamName = args[1];
        String playerName = args[2];

        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        TeamManager teamManager = plugin.getTeamManager();
        if (!teamManager.addPlayerToTeam(teamName, player)) {
            sender.sendMessage("Failed to add player to team.");
            return false;
        }

        sender.sendMessage("Player " + playerName + " added to team " + teamName + ".");
        return true;
    }
}
