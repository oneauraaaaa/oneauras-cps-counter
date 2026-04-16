package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.OneaurasCPSCounter;
import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.config.HudPositionScreen;
import com.oneaura.cpscounter.configlib.ConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ClothConfigScreenBuilder {
	private ClothConfigScreenBuilder() {
	}

	public static Screen create(Screen parent) {
		CPSConfig config = OneaurasCPSCounterClient.config;
		if (config == null) {
			return parent;
		}

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Component.literal("oneaura's CPS Counter Configuration"));
		builder.setSavingRunnable(() -> ConfigManager.save(OneaurasCPSCounter.MOD_ID));

		ConfigEntryBuilder entries = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

		general.addEntry(entries.startBooleanToggle(Component.literal("Enabled"), config.enabled)
				.setDefaultValue(true)
				.setTooltip(Component.literal("Enable or disable the CPS counter on the HUD."))
				.setSaveConsumer(value -> config.enabled = value)
				.build());

		general.addEntry(new ButtonListEntry(
				Component.literal("Display Mode"),
				() -> Component.literal("Mode: " + config.displayMode.name()),
				() -> config.displayMode = nextDisplayMode(config.displayMode)));

		general.addEntry(new ButtonListEntry(
				Component.literal("HUD Position"),
				Component.literal("Reposition"),
				() -> {
					Minecraft client = Minecraft.getInstance();
					Screen current = client.screen;
					client.setScreen(new HudPositionScreen(current));
				}));

		general.addEntry(entries.startStrField(Component.literal("Label Text"), config.labelText)
				.setDefaultValue(" CPS")
				.setTooltip(Component.literal("Text shown after left/right CPS values."))
				.setSaveConsumer(value -> config.labelText = value)
				.build());

		general.addEntry(entries.startStrField(Component.literal("Text Color (RRGGBB)"), config.textColor)
				.setDefaultValue("FFFFFF")
				.setTooltip(Component.literal("Hex text color without #."))
				.setSaveConsumer(value -> config.textColor = value)
				.build());

		general.addEntry(entries.startBooleanToggle(Component.literal("Text Shadow"), config.textShadow)
				.setDefaultValue(true)
				.setTooltip(Component.literal("Draw a shadow under the text."))
				.setSaveConsumer(value -> config.textShadow = value)
				.build());

		general.addEntry(entries.startEnumSelector(Component.literal("Text Style"), CPSConfig.TextStyle.class, config.textStyle)
				.setDefaultValue(CPSConfig.TextStyle.NONE)
				.setTooltip(Component.literal("Choose text style."))
				.setSaveConsumer(value -> config.textStyle = value)
				.build());

		general.addEntry(entries.startBooleanToggle(Component.literal("Background"), config.showBackground)
				.setDefaultValue(true)
				.setTooltip(Component.literal("Render a background behind CPS text."))
				.setSaveConsumer(value -> config.showBackground = value)
				.build());

		general.addEntry(entries.startStrField(Component.literal("Background Color (AARRGGBB)"), config.backgroundColor)
				.setDefaultValue("80000000")
				.setTooltip(Component.literal("Hex background color with alpha, without #."))
				.setSaveConsumer(value -> config.backgroundColor = value)
				.build());

		general.addEntry(entries.startIntField(Component.literal("Background Corner Radius"), config.backgroundCornerRadius)
				.setDefaultValue(0)
				.setMin(0)
				.setMax(20)
				.setTooltip(Component.literal("0 for sharp corners."))
				.setSaveConsumer(value -> config.backgroundCornerRadius = value)
				.build());

		general.addEntry(entries.startIntField(Component.literal("HUD X"), config.hudX)
				.setDefaultValue(5)
				.setMin(0)
				.setMax(10_000)
				.setTooltip(Component.literal("Horizontal HUD position in pixels."))
				.setSaveConsumer(value -> config.hudX = value)
				.build());

		general.addEntry(entries.startIntField(Component.literal("HUD Y"), config.hudY)
				.setDefaultValue(5)
				.setMin(0)
				.setMax(10_000)
				.setTooltip(Component.literal("Vertical HUD position in pixels."))
				.setSaveConsumer(value -> config.hudY = value)
				.build());

		return builder.build();
	}

	private static CPSConfig.DisplayMode nextDisplayMode(CPSConfig.DisplayMode current) {
		return switch (current) {
			case BOTH -> CPSConfig.DisplayMode.LEFT;
			case LEFT -> CPSConfig.DisplayMode.RIGHT;
			case RIGHT -> CPSConfig.DisplayMode.BOTH;
		};
	}
}
