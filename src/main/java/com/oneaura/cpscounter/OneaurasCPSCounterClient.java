package com.oneaura.cpscounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class OneaurasCPSCounterClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("cpscounter");

    public enum HudPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    public static HudPosition hudPosition = HudPosition.TOP_LEFT;
    private static File configFile;

    @Override
    public void onInitializeClient() {
        LOGGER.info("cps counter goess brrrrrrrrrr");

        configFile = new File(MinecraftClient.getInstance().runDirectory, "config/cpscounter.properties");
        loadConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> CPSManager.tick());

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null && client.currentScreen == null) {
                int leftCPS = CPSManager.getLeftCPS();
                int rightCPS = CPSManager.getRightCPS();
                String textToRender = leftCPS + " | " + rightCPS + " CPS";
                int textWidth = client.textRenderer.getWidth(textToRender);

                int x, y;
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                switch (hudPosition) {
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

    public static void loadConfig() {
        Properties properties = new Properties();
        if (configFile.exists()) {
            try (FileInputStream stream = new FileInputStream(configFile)) {
                properties.load(stream);
                String positionFromFile = properties.getProperty("position", "TOP_LEFT");
                hudPosition = HudPosition.valueOf(positionFromFile.toUpperCase());
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Could not load config file, using defaults.", e);
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        Properties properties = new Properties();
        properties.setProperty("position", hudPosition.name());
        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            configFile.getParentFile().mkdirs();
            properties.store(stream, "CPS Counter Config | Available positions: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT");
        } catch (IOException e) {
            LOGGER.error("Could not save config file.", e);
        }
    }
}

