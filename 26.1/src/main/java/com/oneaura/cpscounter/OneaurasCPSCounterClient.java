package com.oneaura.cpscounter;

import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.configlib.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class OneaurasCPSCounterClient implements ClientModInitializer {
	public static CPSConfig config;

	@Override
	public void onInitializeClient() {
		config = ConfigManager.register(OneaurasCPSCounter.MOD_ID, CPSConfig.class)
				.orElseGet(CPSConfig::new);

		ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
		HudElementRegistry.attachElementAfter(
				VanillaHudElements.CHAT,
				OneaurasCPSCounter.id("cps_counter"),
				(drawContext, tickCounter) -> renderHud(drawContext));
	}

	private void onClientTick(Minecraft client) {
		CPSManager.tick();
	}

	private static void renderHud(GuiGraphicsExtractor drawContext) {
		if (config == null || !config.enabled) {
			return;
		}

		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.screen != null) {
			return;
		}

		renderCpsHud(drawContext, client);
	}

	public static void renderCpsHud(GuiGraphicsExtractor drawContext, Minecraft client) {
		int left = CPSManager.getLeftCps();
		int right = CPSManager.getRightCps();
		String content = switch (config.displayMode) {
			case BOTH -> left + " | " + right + config.labelText;
			case LEFT -> left + config.labelText;
			case RIGHT -> right + config.labelText;
		};
		MutableComponent styled = Component.literal(content);
		styled = switch (config.textStyle) {
			case BOLD -> styled.withStyle(ChatFormatting.BOLD);
			case ITALIC -> styled.withStyle(ChatFormatting.ITALIC);
			case UNDERLINED -> styled.withStyle(ChatFormatting.UNDERLINE);
			case NONE -> styled;
		};

		int x = config.hudX;
		int y = config.hudY;
		int textWidth = client.font.width(styled);

		if (config.showBackground) {
			int x1 = x - 2;
			int y1 = y - 2;
			int x2 = x + textWidth + 2;
			int y2 = y + 10;
			int radius = Math.max(0, config.backgroundCornerRadius);
			int color = parseArgb(config.backgroundColor, 0x80000000);

			if (radius <= 0) {
				drawContext.fill(x1, y1, x2, y2, color);
			} else {
				int clampedRadius = Math.min(radius, Math.min((x2 - x1) / 2, (y2 - y1) / 2));
				drawContext.fill(x1 + clampedRadius, y1, x2 - clampedRadius, y2, color);
				drawContext.fill(x1, y1 + clampedRadius, x1 + clampedRadius, y2 - clampedRadius, color);
				drawContext.fill(x2 - clampedRadius, y1 + clampedRadius, x2, y2 - clampedRadius, color);
			}
		}

		drawContext.text(client.font, styled, x, y, parseRgb(config.textColor, 0xFFFFFFFF), config.textShadow);
	}

	private static int parseRgb(String hex, int fallback) {
		try {
			return 0xFF000000 | Integer.parseUnsignedInt(hex.trim(), 16);
		} catch (RuntimeException ignored) {
			return fallback;
		}
	}

	private static int parseArgb(String hex, int fallback) {
		try {
			return (int) Long.parseUnsignedLong(hex.trim(), 16);
		} catch (RuntimeException ignored) {
			return fallback;
		}
	}
}
