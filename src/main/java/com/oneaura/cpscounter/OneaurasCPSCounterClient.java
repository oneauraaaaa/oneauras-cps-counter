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

    // Yeni eklenenler
    public static final String MOD_ID = "cpscounter";
    public static CPSConfig config;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Bu enum burada kalabilir, yeri güzel.
    public enum HudPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    public enum TextStyle {
        NONE, BOLD, ITALIC, UNDERLINED
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("CPS Counter is initializing!");

        // Yeni kütüphanenle ayarları kaydet ve yükle
        config = ConfigManager.register(MOD_ID, CPSConfig.class)
                .orElseThrow(() -> new IllegalStateException("Could not load CPS Counter configuration"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> CPSManager.tick());

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            // Ayarlardan HUD'un aktif olup olmadığını kontrol et
            if (!config.enabled) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null && client.currentScreen == null) {
                int leftCPS = CPSManager.getLeftCPS();
                int rightCPS = CPSManager.getRightCPS();
                String textToRender;
                if (config.showLabel) {
                    textToRender = leftCPS + " | " + rightCPS + " CPS";
                } else {
                    textToRender = leftCPS + " | " + rightCPS;
                }

                int textWidth = client.textRenderer.getWidth(textToRender);
                int padding = config.padding; // Ayarlardan padding değerini al

                int x, y;
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                switch (config.hudPosition) {
                    case TOP_RIGHT:
                        x = screenWidth - textWidth - padding;
                        y = padding;
                        break;
                    case BOTTOM_LEFT:
                        x = padding;
                        y = screenHeight - 10 - padding;
                        break;
                    case BOTTOM_RIGHT:
                        x = screenWidth - textWidth - padding;
                        y = screenHeight - 10 - padding;
                        break;
                    case TOP_LEFT:
                    default:
                        x = padding;
                        y = padding;
                        break;
                }

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
                        // Do nothing
                        break;
                }

                int textColor;
                try {
                    textColor = 0xFF000000 | Integer.parseInt(config.textColor.trim(), 16);
                } catch (NumberFormatException e) {
                    textColor = 0xFFFFFFFF; //
                }
                if (config.showBackground) {
                    int backgroundColor;
                    try {
                        backgroundColor = (int) Long.parseLong(config.backgroundColor.trim(), 16);
                    } catch (NumberFormatException e) {
                        backgroundColor = 0x80000000; // Varsayılan: Yarı şeffaf siyah
                    }

                    int x1 = x - 2;
                    int y1 = y - 2;
                    int x2 = x + textWidth + 2;
                    int y2 = y + 10;
                    int radius = config.backgroundCornerRadius;

                    if (radius <= 0) {
                        drawContext.fill(x1, y1, x2, y2, backgroundColor);
                    } else {
                        // Yumuşak kenarlı dikdörtgen çizimi
                        drawContext.fill(x1 + radius, y1, x2 - radius, y2, backgroundColor); // Orta dikey bölüm
                        drawContext.fill(x1, y1 + radius, x1 + radius, y2 - radius, backgroundColor); // Sol bölüm
                        drawContext.fill(x2 - radius, y1 + radius, x2, y2 - radius, backgroundColor); // Sağ bölüm
                    }
                }

                drawContext.drawText(client.textRenderer, textToRender, x, y, textColor, config.textShadow);
            }
        });
    }

    // Eski loadConfig ve saveConfig metodları tamamen silindi.
}
