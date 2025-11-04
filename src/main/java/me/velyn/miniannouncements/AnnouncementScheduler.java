package me.velyn.miniannouncements;

import me.velyn.miniannouncements.config.PluginConfig;
import me.velyn.miniannouncements.minimessage.MiniMessageContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;

public class AnnouncementScheduler {

    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;
    private final Log log;
    private final PluginConfig config;
    private final MiniMessageContainer miniMsgContainer;

    private final Set<Integer> scheduleIds = new HashSet<>();

    public AnnouncementScheduler(JavaPlugin plugin, Log log, PluginConfig config, MiniMessageContainer miniMsgContainer) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.log = log;
        this.config = config;
        this.miniMsgContainer = miniMsgContainer;
    }

    public void start() {
        for (var announcement : config.getScheduledAnnouncements()) {
            if (!announcement.enabled()) {
                continue;
            }
            int id = scheduler.scheduleSyncRepeatingTask(
                    plugin,
                    () -> sendAnnouncement(announcement),
                    // First execution after 10 seconds
                    10*20,
                    announcement.delaySeconds() * 20L);
            scheduleIds.add(id);
            log.infoF("Scheduled announcement '%s' every %d seconds", announcement.name(), announcement.delaySeconds());
        }
        log.infoF("Started %d scheduled announcements", scheduleIds.size());
    }

    private void sendAnnouncement(Announcement announcement) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            log.debugF("Sending announcement '%s' to player '%s'", announcement.name(), player.getName());
            player.sendMessage(miniMsgContainer.getMiniMessage().deserialize(announcement.miniMsg()));
        });
    }

    public void stop() {
        scheduleIds.forEach(scheduler::cancelTask);
        scheduleIds.clear();
    }
}
