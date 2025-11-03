package me.velyn.miniannouncements;

public record Announcement(
        String name,
        boolean enabled,
        int delaySeconds,
        String miniMsg
) {}
