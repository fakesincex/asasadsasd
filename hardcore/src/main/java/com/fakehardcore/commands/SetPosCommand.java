package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import com.fakehardcore.utils.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class SetPosCommand implements CommandExecutor {
    private FakeHardcore plugin;

    public SetPosCommand(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length != 5) {
            sender.sendMessage("Использование: /hc setpos <team> <x> <y> <z>");
            return false;
        }

        String teamName = args[1];
        double x, y, z;
        try {
            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Неверные координаты.");
            return false;
        }

        Location loc = new Location(((Player) sender).getWorld(), x, y, z);

        TeamManager teamManager = plugin.getTeamManager();
        teamManager.setTeamLocation(teamName, loc);

        plugin.getConfigManager().saveTeamLocation(teamName, loc);

        sender.sendMessage("Координаты для команды " + teamName + " установлены.");
        return true;
    }
}
