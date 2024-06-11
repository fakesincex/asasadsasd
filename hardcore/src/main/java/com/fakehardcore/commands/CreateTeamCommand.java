package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import com.fakehardcore.utils.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.ChatColor;

public class CreateTeamCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public CreateTeamCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("Usage: /hc create <team> <color>");
            return false;
        }

        String teamName = args[1];
        String colorName = args[2].toUpperCase();

        ChatColor color;
        try {
            color = ChatColor.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Invalid color name.");
            return false;
        }

        TeamManager teamManager = plugin.getTeamManager();
        teamManager.createTeam(teamName, color);

        sender.sendMessage("Team " + teamName + " created with color " + color.name() + ".");
        return true;
    }
}
