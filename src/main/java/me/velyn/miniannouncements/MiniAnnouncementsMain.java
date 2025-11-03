package me.velyn.miniannouncements;

import me.velyn.miniannouncements.commands.AdminCommand;
import me.velyn.miniannouncements.config.PluginConfig;
import me.velyn.miniannouncements.minimessage.MiniMessageContainer;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MiniAnnouncementsMain extends JavaPlugin {

    private final PluginConfig pluginConfig;
    private Log log;
    private AnnouncementScheduler announcementScheduler;

    public MiniAnnouncementsMain() {
        pluginConfig = new PluginConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();
        log = new Log(getLogger());
        updateConfig();

        pluginConfig.applyValuesFrom(getConfig());

        String fallbackPrefix = getName().toLowerCase();
        CommandMap cm = getServer().getCommandMap();
        cm.register(fallbackPrefix, new AdminCommand(this));

        MiniMessageContainer miniMessageContainer = new MiniMessageContainer();

        announcementScheduler = new AnnouncementScheduler(this, log, pluginConfig, miniMessageContainer);
        announcementScheduler.start();
    }

    private void updateConfig() {
        FileConfiguration config = getConfig();
        InputStream resource = this.getResource("config.yml");

        if (resource == null) {
            log.warnF("Could not update Config because the default Config could not be found!");
            return;
        }
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resource));

        boolean anyUpdated = false;
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key, true)) {
                config.set(key, defaultConfig.get(key));
                anyUpdated = true;
            }
        }
        if (anyUpdated) {
            log.infoF("New Config Values have been added to your Configuration File. You might want to check those.");
        }
        saveConfig();
    }

    @Override
    public void reloadConfig() {
        log.infoF("Reloading Config...");
        super.reloadConfig();
        pluginConfig.applyValuesFrom(getConfig());
        log.setDebug(pluginConfig.isDebug());
        log.infoF("Config reloaded!");
        if (announcementScheduler != null) {
            announcementScheduler.stop();
            announcementScheduler.start();
            log.infoF("Scheduler restarted!");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        announcementScheduler.stop();
    }
}
