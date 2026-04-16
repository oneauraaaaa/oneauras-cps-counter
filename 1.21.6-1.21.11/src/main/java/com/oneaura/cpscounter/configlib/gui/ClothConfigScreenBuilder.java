package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.config.HudPositionScreen;
import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;
import com.oneaura.cpscounter.configlib.ConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

public class ClothConfigScreenBuilder {

    // Fields managed by the drag screen — hidden from normal config entries
    private static final Set<String> HIDDEN_FIELDS = Set.of("hudX", "hudY", "displayMode");

    public static Screen create(Screen parent, String modId) {
        ConfigHolder config = ConfigManager.get(modId);
        if (config == null) {
            return new net.minecraft.client.gui.screen.TitleScreen();
        }

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal(modId + " Configuration"));

        builder.setSavingRunnable(() -> {
            ConfigManager.save(modId);
        });

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Use reflection to automatically create entries for each field
        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (HIDDEN_FIELDS.contains(field.getName())) {
                continue;
            }

            Optional<Comment> comment = Optional.ofNullable(field.getAnnotation(Comment.class));

            try {
                if (field.getType() == String.class) {
                    general.addEntry(
                            entryBuilder
                                    .startStrField(Text.literal(formatFieldName(field.getName())),
                                            (String) field.get(config))
                                    .setDefaultValue((String) field.get(config))
                                    .setTooltip(comment.map(c -> new Text[] { Text.of(c.value()) }))
                                    .setSaveConsumer(newValue -> setField(field, config, newValue))
                                    .build());
                } else if (field.getType() == int.class) {
                    general.addEntry(entryBuilder
                            .startIntField(Text.literal(formatFieldName(field.getName())), (int) field.get(config))
                            .setDefaultValue((int) field.get(config))
                            .setTooltip(comment.map(c -> new Text[] { Text.of(c.value()) }))
                            .setSaveConsumer(newValue -> setField(field, config, newValue))
                            .build());
                } else if (field.getType() == boolean.class) {
                    general.addEntry(
                            entryBuilder
                                    .startBooleanToggle(Text.literal(formatFieldName(field.getName())),
                                            (boolean) field.get(config))
                                    .setDefaultValue((boolean) field.get(config))
                                    .setTooltip(comment.map(c -> new Text[] { Text.of(c.value()) }))
                                    .setSaveConsumer(newValue -> setField(field, config, newValue))
                                    .build());
                } else if (field.getType().isEnum()) {
                    general.addEntry(entryBuilder
                            .startEnumSelector(Text.literal(formatFieldName(field.getName())),
                                    (Class<Enum>) field.getType(),
                                    (Enum) field.get(config))
                            .setDefaultValue((Enum) field.get(config))
                            .setTooltip(comment.map(c -> new Text[] { Text.of(c.value()) }))
                            .setSaveConsumer(newValue -> setField(field, config, newValue))
                            .build());
                }

                // Inject the button right after the "enabled" field
                if (field.getName().equals("enabled")) {
                    general.addEntry(new ButtonListEntry(
                            Text.literal("Display Mode"),
                            () -> Text.literal("Mode: " + ((CPSConfig) config).displayMode.name()),
                            () -> {
                                CPSConfig cpsConfig = (CPSConfig) config;
                                cpsConfig.displayMode = nextDisplayMode(cpsConfig.displayMode);
                            }));

                    general.addEntry(new ButtonListEntry(
                            Text.literal("Reposition HUD"),
                            Text.literal("Reposition"),
                            () -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                Screen configScreen = mc.currentScreen;
                                mc.execute(() -> mc.setScreen(new HudPositionScreen(configScreen)));
                            }));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    private static CPSConfig.DisplayMode nextDisplayMode(CPSConfig.DisplayMode current) {
        return switch (current) {
            case BOTH -> CPSConfig.DisplayMode.LEFT;
            case LEFT -> CPSConfig.DisplayMode.RIGHT;
            case RIGHT -> CPSConfig.DisplayMode.BOTH;
        };
    }

    private static String formatFieldName(String name) {
        if (name == null || name.isEmpty())
            return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).replaceAll("([A-Z])", " $1");
    }

    private static void setField(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
