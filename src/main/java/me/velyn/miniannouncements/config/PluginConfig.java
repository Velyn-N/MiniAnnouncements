package me.velyn.miniannouncements.config;

import me.velyn.miniannouncements.Announcement;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class PluginConfig {

    private boolean isDebug;
    private Set<Announcement> scheduledAnnouncements;

    public void applyValuesFrom(ConfigurationSection config) {
        this.isDebug = config.getBoolean("debug", false);

        this.scheduledAnnouncements = new HashSet<>();
        ConfigurationSection scheduledSection = config.getConfigurationSection("announcements.scheduled");
        if (scheduledSection != null) {
            for (var key : scheduledSection.getKeys(false)) {
                ConfigurationSection announcementSection = scheduledSection.getConfigurationSection(key);
                if (announcementSection == null) {
                    continue;
                }
                scheduledAnnouncements.add(new Announcement(
                        key,
                        announcementSection.getBoolean("enabled"),
                        announcementSection.getInt("delay"),
                        announcementSection.getString("message")
                ));
            }
        }
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Set<Announcement> getScheduledAnnouncements() {
        return scheduledAnnouncements;
    }
}
