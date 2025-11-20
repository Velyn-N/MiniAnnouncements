package me.velyn.miniannouncements.minimessage;

import me.velyn.miniannouncements.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.function.*;

public class AnnouncementSpecialTags implements TagResolver {

    public static final DateTimeFormatter NICE_ISO_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Map<String, Function<Ctx, Component>> tags;
    private static Map<String, BiFunction<Ctx, ArgumentQueue, Component>> argTags;

    private final Ctx context;

    public AnnouncementSpecialTags(Ctx context) {
        this.context = context;
        if (tags == null) {
            tags = buildTagHandlerMap();
        }
        if (argTags == null) {
            argTags = buildArgTagHandlerMap();
        }
    }

    public record Ctx(
            Player recipient
    ) {}

    private static Map<String, Function<Ctx, Component>> buildTagHandlerMap() {
        Map<String, Function<Ctx, Component>> map = new LinkedHashMap<>();
        map.put("player_name", c -> requireRecipient(c,
                ctx -> Component.text(ctx.recipient().getName())));
        map.put("player_display_name", c -> requireRecipient(c,
                ctx -> ctx.recipient().displayName()));
        map.put("player_uuid", c -> requireRecipient(c,
                ctx -> Component.text(ctx.recipient().getUniqueId().toString())));
        return map;
    }

    private static Map<String, BiFunction<Ctx, ArgumentQueue, Component>> buildArgTagHandlerMap() {
        Log log = MiniAnnouncementsMain.getLog();
        Map<String, BiFunction<Ctx, ArgumentQueue, Component>> map = new LinkedHashMap<>();
        map.put("date_time", (c, a) -> {
            DateTimeFormatter format = NICE_ISO_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.now();
            if (a.hasNext()) {
                String pattern = a.pop().value();
                if (!List.of("iso", "def", "default").contains(pattern)) {
                    try {
                        format = DateTimeFormatter.ofPattern(pattern);
                    } catch (IllegalArgumentException e) {
                        log.errorF("Invalid DateTime Format received for MiniMessage Tag: %s", pattern);
                        return null;
                    }
                }
            }
            if (a.hasNext()) {
                String dateStr = a.pop().value();
                try {
                    dateTime = LocalDateTime.parse(dateStr, NICE_ISO_DATE_TIME);
                } catch (DateTimeParseException e) {
                    log.errorF("Invalid DateTime Format received for MiniMessage Tag: %s", dateStr);
                    return null;
                }
            }
            return Component.text(dateTime.format(format));
        });
        map.put("date", (c, a) -> {
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
            LocalDate date = LocalDate.now();
            if (a.hasNext()) {
                String pattern = a.pop().value();
                if (!List.of("iso", "def", "default").contains(pattern)) {
                    try {
                        format = DateTimeFormatter.ofPattern(pattern);
                    } catch (IllegalArgumentException e) {
                        log.errorF("Invalid Date Format received for MiniMessage Tag: %s", pattern);
                        return null;
                    }
                }
            }
            if (a.hasNext()) {
                String dateStr = a.pop().value();
                try {
                    date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
                } catch (DateTimeParseException e) {
                    log.errorF("Invalid Date Format received for MiniMessage Tag: %s", dateStr);
                    return null;
                }
            }
            return Component.text(date.format(format));
        });
        return map;
    }

    private static Component requireRecipient(Ctx ctx, Function<Ctx, Component> function) {
        return ctx.recipient() == null ? null : function.apply(ctx);
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if (tags.containsKey(name)) {
            return Tag.inserting(tags.get(name).apply(context));
        }
        if (argTags.containsKey(name)) {
            return Tag.inserting(argTags.get(name).apply(context, arguments));
        }
        return null;
    }

    @Override
    public boolean has(@NotNull String name) {
        return tags.containsKey(name) || argTags.containsKey(name);
    }
}
