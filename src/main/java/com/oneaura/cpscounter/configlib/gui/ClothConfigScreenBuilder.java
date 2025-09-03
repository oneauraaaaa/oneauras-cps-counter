package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;
import com.oneaura.cpscounter.configlib.ConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Optional;

public class ClothConfigScreenBuilder {

    public static Screen create(Screen parent, String modId) {
        ConfigHolder config = ConfigManager.get(modId);
        if (config == null) {
            return new net.minecraft.client.gui.screen.TitleScreen(); // Placeholder for a proper error screen
        }

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal(modId + " Configuration"));

        // Save callback
        builder.setSavingRunnable(() -> {
            ConfigManager.save(modId);
        });

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Use reflection to automatically create entries for each field in the config
        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Optional<Comment> comment = Optional.ofNullable(field.getAnnotation(Comment.class));

            try {
                // Add a text input for String fields
                if (field.getType() == String.class) {
                    general.addEntry(entryBuilder.startStrField(Text.literal(field.getName()), (String) field.get(config))
                            .setDefaultValue((String) field.get(config)) // Default value for reset button
                            .setTooltip(comment.map(c -> new Text[]{Text.of(c.value())})) // Use @Comment for tooltip
                            .setSaveConsumer(newValue -> setField(field, config, newValue)) // Update the field
                            .build());
                }
                // Add a number input for Integer fields
                else if (field.getType() == int.class) {
                    general.addEntry(entryBuilder.startIntField(Text.literal(field.getName()), (int) field.get(config))
                            .setDefaultValue((int) field.get(config))
                            .setTooltip(comment.map(c -> new Text[]{Text.of(c.value())}))
                            .setSaveConsumer(newValue -> setField(field, config, newValue))
                            .build());
                }
                // Add a checkbox for boolean fields
                else if (field.getType() == boolean.class) {
                    general.addEntry(entryBuilder.startBooleanToggle(Text.literal(field.getName()), (boolean) field.get(config))
                            .setDefaultValue((boolean) field.get(config))
                            .setTooltip(comment.map(c -> new Text[]{Text.of(c.value())}))
                            .setSaveConsumer(newValue -> setField(field, config, newValue))
                            .build());
                }
                // *** YENİ EKLENEN KISIM: Enum'lar için dropdown menüsü ***
                else if (field.getType().isEnum()) {
                    general.addEntry(entryBuilder.startEnumSelector(Text.literal(field.getName()), (Class<Enum>) field.getType(), (Enum) field.get(config))
                            .setDefaultValue((Enum) field.get(config))
                            .setTooltip(comment.map(c -> new Text[]{Text.of(c.value())}))
                            .setSaveConsumer(newValue -> setField(field, config, newValue))
                            .build());
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    private static void setField(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
