package me.velyn.miniannouncements.config;

import org.bukkit.configuration.ConfigurationSection;

public class PluginConfig {

    private boolean isDebug;

    public void applyValuesFrom(ConfigurationSection config) {
        this.isDebug = config.getBoolean("debug", false);
    }

    public boolean isDebug() {
        return isDebug;
    }


}
