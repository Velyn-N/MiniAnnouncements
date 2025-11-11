package me.velyn.miniannouncements.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class AnnouncementSpecialTags implements TagResolver {

    private static Map<String, Function<Ctx, Component>> tags;

    private final Ctx context;

    public AnnouncementSpecialTags(Ctx context) {
        this.context = context;
        if (tags == null) {
            tags = buildTagHandlerMap();
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

    private static Component requireRecipient(Ctx ctx, Function<Ctx, Component> function) {
        return ctx.recipient() == null ? null : function.apply(ctx);
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if (tags.containsKey(name)) {
            return Tag.inserting(tags.get(name).apply(context));
        }
        return null;
    }

    @Override
    public boolean has(@NotNull String name) {
        return tags.containsKey(name);
    }
}
