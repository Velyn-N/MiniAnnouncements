package me.velyn.miniannouncements.commands;

import me.velyn.miniannouncements.MiniAnnouncementsMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminCommand extends Command {
    private static final String PERMISSION = "miniannouncements.commands.admin";

    private final MiniAnnouncementsMain main;

    public AdminCommand(MiniAnnouncementsMain main) {
        super(main.getName().toLowerCase());
        this.main = main;
        setPermission(PERMISSION);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return List.of("reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage(Component.text("You do not have the Permission to use this Command!", NamedTextColor.RED));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(Component.text("Wrong Syntax", NamedTextColor.RED));
            return false;
        }
        switch (args[0]) {
            case "reload" -> {
                main.reloadConfig();
                return true;
            }
            default -> {
                sender.sendMessage(Component.text("Unknown SubCommand", NamedTextColor.RED));
                return false;
            }
        }
    }
}
