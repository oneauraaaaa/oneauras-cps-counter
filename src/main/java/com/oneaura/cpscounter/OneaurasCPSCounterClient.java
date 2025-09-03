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

// git wait

public class OneaurasCPSCounterClient implements ClientModInitializer {

    // Yeni eklenenler
    public static final String MOD_ID = "cpscounter";
    public static CPSConfig config;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Bu enum burada kalabilir, yeri güzel.
    public enum HudPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
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

                int x, y;
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Konumu yeni ayar dosyasından oku
                switch (config.hudPosition) {
                    case TOP_RIGHT:
                        x = screenWidth - textWidth - 5;
                        y = 5;
                        break;
                    case BOTTOM_LEFT:
                        x = 5;
                        y = screenHeight - 15;
                        break;
                    case BOTTOM_RIGHT:
                        x = screenWidth - textWidth - 5;
                        y = screenHeight - 15;
                        break;
                    case TOP_LEFT:
                    default:
                        x = 5;
                        y = 5;
                        break;
                }

                int textColor = 0xFFFFFFFF;
                int backgroundColor = 0x80000000;

                drawContext.fill(x - 1, y - 1, x + textWidth + 1, y + 10, backgroundColor);
                drawContext.drawText(client.textRenderer, textToRender, x, y, textColor, false);
            }
        });
    }

    // Eski loadConfig ve saveConfig metodları tamamen silindi.
}
