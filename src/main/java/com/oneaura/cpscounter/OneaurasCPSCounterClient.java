package com.oneaura.cpscounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneaurasCPSCounterClient implements ClientModInitializer {

    public static final Logger
            LOGGER
            = LoggerFactory.
            getLogger
                    ("cpscounter");

    @Override
    public void onInitializeClient() {

        LOGGER
                .info("CPS Counter modu başlatılıyor!");

        // Her tick CPS sayımını güncelle
        ClientTickEvents.
                END_CLIENT_TICK
                .register(client -> CPSManager.
                        tick
                                ());

        // HUD render
        HudRenderCallback.
                EVENT
                .register((drawContext, tickDelta) -> {
                    MinecraftClient client = MinecraftClient.
                            getInstance
                                    ();

                    if (client.player != null && client.currentScreen == null) {
                        int cps = CPSManager.
                                getCPS
                                        ();
                        String textToRender = "CPS: " + cps;

                        int textColor = 0xFFFFFFFF; // Beyaz
                        int backgroundColor = 0x80000000; // Yarı saydam siyah
                        int textWidth = client.textRenderer.getWidth(textToRender);
                        int textHeight = client.textRenderer.fontHeight;

                        // Arka plan kutusu
                        drawContext.fill(4, 4, 6 + textWidth, 6 + textHeight, backgroundColor);

                        // Yazıyı çiz (shadow true ile)
                        drawContext.drawText(
                                client.textRenderer,
                                textToRender,
                                5,
                                5,
                                textColor,
                                true // shadow
                        );
                    }
                });
    }
}