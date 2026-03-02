package com.oneaura.cpscounter;

import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.configlib.ConfigManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneaurasCPSCounterClient implements ClientModInitializer {

    public static final String MOD_ID = "cpscounter";
    public static CPSConfig config;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public enum TextStyle {
        NONE, BOLD, ITALIC, UNDERLINED
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("CPS Counter is initializing!");

        config = ConfigManager.register(MOD_ID, CPSConfig.class)
                .orElseThrow(() -> new IllegalStateException("Could not load CPS Counter configuration"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> CPSManager.tick());

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!config.enabled) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null && client.currentScreen == null) {
                renderCpsHud(drawContext, client);
            }
        });
    }

    /**
     * Renders the CPS HUD at the configured position.
     * This is also called by HudPositionScreen for the live preview.
     */
    public static void renderCpsHud(DrawContext drawContext, MinecraftClient client) {
        int leftCPS = CPSManager.getLeftCPS();
        int rightCPS = CPSManager.getRightCPS();
        String textToRender = leftCPS + " | " + rightCPS + config.labelText;

        int textWidth = client.textRenderer.getWidth(textToRender);
        int padding = config.padding;

        int x = config.hudX + padding;
        int y = config.hudY + padding;

        switch (config.textStyle) {
            case BOLD:
                textToRender = "§l" + textToRender;
                break;
            case ITALIC:
                textToRender = "§o" + textToRender;
                break;
            case UNDERLINED:
                textToRender = "§n" + textToRender;
                break;
            case NONE:
            default:
                break;
        }

        int textColor;
        try {
            textColor = 0xFF000000 | Integer.parseInt(config.textColor.trim(), 16);
        } catch (NumberFormatException e) {
            textColor = 0xFFFFFFFF;
        }
        if (config.showBackground) {
            int backgroundColor;
            try {
                backgroundColor = (int) Long.parseLong(config.backgroundColor.trim(), 16);
            } catch (NumberFormatException e) {
                backgroundColor = 0x80000000;
            }

            int x1 = x - 2;
            int y1 = y - 2;
            int x2 = x + textWidth + 2;
            int y2 = y + 10;
            int radius = config.backgroundCornerRadius;

            if (radius <= 0) {
                drawContext.fill(x1, y1, x2, y2, backgroundColor);
            } else {
                drawContext.fill(x1 + radius, y1, x2 - radius, y2, backgroundColor);
                drawContext.fill(x1, y1 + radius, x1 + radius, y2 - radius, backgroundColor);
                drawContext.fill(x2 - radius, y1 + radius, x2, y2 - radius, backgroundColor);
            }
        }

        drawContext.drawText(client.textRenderer, textToRender, x, y, textColor, config.textShadow);
    }
}
