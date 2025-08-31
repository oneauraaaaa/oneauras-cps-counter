package com.oneaura.cpscounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneaurasCPSCounterClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("cpscounter");

    @Override
    public void onInitializeClient() {
        LOGGER.info("CPS Counter modu başlatılıyor!");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            CPSManager.tick();
        });

        // Ekrana her frame çizim yapılırken bu kod çalışır.
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            // Sadece bir dünya açıkken ve ekranda menü yokken çizim yap.
            if (client.player != null && client.currentScreen == null) {
                int leftCps = CPSManager.getLeftCPS();
                int rightCps = CPSManager.getRightCPS();
                // Klavyede bulamadığın o düz çizgi '|' karakteridir.
                String textToRender = leftCps + " | " + rightCps + " CPS";

                // --- MODERN VE HATASIZ ÇİZİM KODU ---
                int textColor = 0xFFFFFFFF; // %100 Opak Beyaz renk
                int backgroundColor = 0x80000000; // Yarı saydam siyah arka plan
                int textWidth = client.textRenderer.getWidth(textToRender);
                int textHeight = client.textRenderer.fontHeight;

                // Önce metnin arkasına bir kutu çiziyoruz.
                drawContext.fill(4, 4, 5 + textWidth + 1, 4 + textHeight + 1, backgroundColor);

                // Sonra metni bu kutunun üzerine, gölgesiz bir şekilde çiziyoruz.
                drawContext.drawText(
                        client.textRenderer,
                        textToRender,
                        5,          // X Konumu
                        5,          // Y Konumu
                        textColor,
                        false       // Gölge yok
                );
            }
        });
    }
}

