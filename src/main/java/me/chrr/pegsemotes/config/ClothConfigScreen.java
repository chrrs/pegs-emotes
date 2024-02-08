package me.chrr.pegsemotes.config;

import me.chrr.pegsemotes.emote.RepositoryManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ClothConfigScreen {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.ConfigScreen");

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.pegs-emotes.options"));

        builder.setSavingRunnable(() -> {
            try {
                Config.getInstance().save();
            } catch (IOException e) {
                LOGGER.error("could not save config", e);
            }
        });

        ConfigCategory category = builder.getOrCreateCategory(Text.of("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        category.addEntry(entryBuilder.startTextDescription(
                        Text.translatable("option.pegs-emotes.active", RepositoryManager.getInstance().getEmoteNames().size()).formatted(Formatting.GREEN)
                )
                .setTooltip(Text.translatable("option.pegs-emotes.active.tooltip"))
                .build());

        category.addEntry(entryBuilder.startStrList(
                        Text.translatable("option.pegs-emotes.repositories"),
                        Config.getInstance().repositories
                )
                .setDefaultValue(Config.DEFAULT.repositories)
                .setSaveConsumer((value) -> {
                    Config.getInstance().repositories = value;
                    RepositoryManager.getInstance().reload();
                })
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.pegs-emotes.emote-shadow"),
                        Config.getInstance().emoteShadow
                )
                .setDefaultValue(Config.DEFAULT.emoteShadow)
                .setSaveConsumer((value) -> Config.getInstance().emoteShadow = value)
                .build());

        return builder.build();
    }
}
