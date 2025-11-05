package me.velyn.miniannouncements.commands;

import me.velyn.miniannouncements.Announcement;
import me.velyn.miniannouncements.MiniAnnouncementsMain;
import me.velyn.miniannouncements.config.PluginConfig;
import me.velyn.miniannouncements.minimessage.MiniMessageContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class MiniAnnouncementsCommand extends Command {
    private static final String PERMISSION = "miniannouncements.commands.admin";

    private final MiniAnnouncementsMain main;
    private final PluginConfig config;
    private final MiniMessageContainer miniMsgContainer;

    public MiniAnnouncementsCommand(MiniAnnouncementsMain main, PluginConfig config, MiniMessageContainer miniMsgContainer) {
        super(main.getName().toLowerCase());
        this.main = main;
        this.config = config;
        this.miniMsgContainer = miniMsgContainer;
        setPermission(PERMISSION);
        setAliases(List.of("miniannouncements", "miniAnnounce", "maAdmin"));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> proposals = List.of();
        String alreadyTyped = null;
        if (args.length < 2) {
            proposals = List.of("reload", "send");
            if (args.length == 1) {
                alreadyTyped = args[0];
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("send")) {
            proposals = config.getScheduledAnnouncements().stream().map(Announcement::name).toList();
            alreadyTyped = args[1];
        } else if (args.length == 3 && args[0].equalsIgnoreCase("send")) {
            proposals = super.tabComplete(sender, alias, args);
            alreadyTyped = args[2];
        }

        if (alreadyTyped == null) {
            return proposals;
        }
        final String finalAlreadyTyped = alreadyTyped;
        return proposals.stream()
                .filter(a -> StringUtils.containsIgnoreCase(a, finalAlreadyTyped))
                .sorted(Comparator.comparingInt(a -> StringUtils.indexOfIgnoreCase(a, finalAlreadyTyped)))
                .toList();
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
                sender.sendMessage(Component.text("Config reloaded!", NamedTextColor.GREEN));
                return true;
            }
            case "send" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Wrong Syntax", NamedTextColor.RED));
                    return false;
                }
                String announcementName = args[1];
                var optAnnouncement = config.getScheduledAnnouncements()
                        .stream()
                        .filter(a -> a.name().equalsIgnoreCase(announcementName))
                        .findAny();
                if (optAnnouncement.isEmpty()) {
                    sender.sendMessage(Component.text("Unknown Announcement", NamedTextColor.RED));
                    return false;
                }
                if (args.length == 3) {
                    String playerName = args[2];
                    var optPlayer = Bukkit.getOnlinePlayers().stream()
                            .filter(p -> p.getName().equalsIgnoreCase(playerName))
                            .findAny();
                    if (optPlayer.isEmpty()) {
                        sender.sendMessage(Component.text("Unknown Player", NamedTextColor.RED));
                        return false;
                    }
                    optPlayer.get().sendMessage(miniMsgContainer.getMiniMessage().deserialize(optAnnouncement.get().miniMsg()));
                    sender.sendMessage(Component.text("Sent announcement to Player " + playerName, NamedTextColor.GREEN));
                    return true;
                }
                sender.sendMessage(miniMsgContainer.getMiniMessage().deserialize(optAnnouncement.get().miniMsg()));
                return true;
            }
            default -> {
                sender.sendMessage(Component.text("Unknown SubCommand", NamedTextColor.RED));
                return false;
            }
        }
    }
}
