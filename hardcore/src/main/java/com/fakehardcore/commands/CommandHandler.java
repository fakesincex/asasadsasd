package com.fakehardcore.commands;

import com.fakehardcore.FakeHardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private FakeHardcore plugin;

    public CommandHandler(FakeHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        boolean isOpCommand = args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("team") ||
                args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("setpos") ||
                args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("delete") ||
                args[0].equalsIgnoreCase("reload");

        if (isOpCommand && (!(sender instanceof Player) || !sender.isOp())) {
            sender.sendMessage("§cУ вас нет прав для выполнения этой команды.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return new CreateTeamCommand(plugin).onCommand(sender, command, label, args);
            case "team":
                return new AddPlayerToTeamCommand(plugin).onCommand(sender, command, label, args);
            case "start":
                return new StartCommand(plugin).onCommand(sender, command, label, args);
            case "setpos":
                return new SetPosCommand(plugin).onCommand(sender, command, label, args);
            case "join":
                return new JoinTeamCommand(plugin).onCommand(sender, command, label, args);
            case "cancel":
                return new CancelCommand(plugin).onCommand(sender, command, label, args);
            case "delete":
                return new DeleteTeamCommand(plugin).onCommand(sender, command, label, args);
            case "reload":
                return new ReloadCommand(plugin).onCommand(sender, command, label, args);
            case "toggle":
                return new ToggleHighlightCommand(plugin).onCommand(sender, command, label, args);
            default:
                sender.sendMessage("Неизвестная команда. Используйте /hc для помощи.");
                return false;
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§aКоманды FakeHardcore:");
        sender.sendMessage("§b/hc create <team> <color> §f- Создать команду с указанным именем и цветом.");
        sender.sendMessage("§b/hc team <team> <nickname> §f- Добавить игрока в команду и установить его цвет.");
        sender.sendMessage("§b/hc start §f- Запустить игру и таймер.");
        sender.sendMessage("§b/hc setpos <team> <x> <y> <z> §f- Установить координаты для точки спауна команды.");
        sender.sendMessage("§b/hc join <team> §f- Присоединиться к указанной команде.");
        sender.sendMessage("§b/hc cancel §f- Отменить игру и вернуть игроков на их позиции.");
        sender.sendMessage("§b/hc delete <team> §f- Удалить команду.");
        sender.sendMessage("§b/hc reload §f- Перезагрузить конфигурационные файлы.");
        sender.sendMessage("§b/hc toggle §f- Переключить подсветку для игроков вашей команды.");
    }
}
