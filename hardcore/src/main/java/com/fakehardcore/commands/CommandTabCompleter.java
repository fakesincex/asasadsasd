package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTabCompleter implements TabCompleter {
    private FakeHardcore plugin;

    public CommandTabCompleter(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) {
                completions.addAll(Arrays.asList("create", "team", "start", "setpos", "join", "cancel", "delete", "reload"));
            } else {
                completions.add("join");
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("setpos") || args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("delete"))) {
            completions.addAll(plugin.getTeamManager().getTeams());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            for (String color : new String[]{"AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW"}) {
                completions.add(color);
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("team")) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("setpos")) {
            Player player = (Player) sender;
            completions.add(String.valueOf(player.getLocation().getBlockX()));
            completions.add(String.valueOf(player.getLocation().getBlockY()));
            completions.add(String.valueOf(player.getLocation().getBlockZ()));
        }

        return completions;
    }
}
