package me.velyn.miniannouncements.minimessage;

import me.velyn.miniannouncements.minimessage.resolver.TestResolver;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class MiniMessageContainer {

    private final MiniMessage miniMessage;

    public MiniMessageContainer() {
//        this.miniMessage = MiniMessage.builder()
//                .tags(TagResolver.standard())
//                .editTags(tagResolver -> {
//                    tagResolver.resolver(new TestResolver());
//                })
//                .build();
        this.miniMessage = MiniMessage.miniMessage();
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
}
