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
        LOGGER.info("CPS Counter is initializing!");

        // Config dosyasının yolunu ayarla ve log'a yazdır
        File runDir = MinecraftClient.getInstance().runDirectory;
        LOGGER.info("Minecraft run directory: {}", runDir.getAbsolutePath());
        configFile = new File(runDir, "config/cpscounter.properties");
        LOGGER.info("Config file path set to: {}", configFile.getAbsolutePath());

        loadConfig(); // Ayarları dosyadan yükle

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
        LOGGER.info("Attempting to load config file...");
        Properties properties = new Properties();
        if (configFile.exists()) {
            LOGGER.info("Config file found. Reading properties...");
            try (FileInputStream stream = new FileInputStream(configFile)) {
                properties.load(stream);
                String positionFromFile = properties.getProperty("position", "TOP_LEFT");
                hudPosition = HudPosition.valueOf(positionFromFile.toUpperCase());
                LOGGER.info("Config loaded successfully. Position set to {}.", hudPosition);
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Could not load config file, using defaults.", e);
            }
        } else {
            LOGGER.info("Config file not found. Creating a new one with default values...");
            saveConfig();
        }
    }

    public static void saveConfig() {
        LOGGER.info("Attempting to save config file...");
        Properties properties = new Properties();
        properties.setProperty("position", hudPosition.name());
        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            File parentDir = configFile.getParentFile();
            if (!parentDir.exists()) {
                LOGGER.info("Config directory does not exist. Creating directory: {}", parentDir.getAbsolutePath());
                if (parentDir.mkdirs()) {
                    LOGGER.info("Config directory created successfully.");
                } else {
                    LOGGER.error("FAILED TO CREATE CONFIG DIRECTORY!");
                }
            }
            properties.store(stream, "CPS Counter Config | Available positions: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT");
            LOGGER.info("Config file saved successfully to {}.", configFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("COULD NOT SAVE CONFIG FILE.", e);
        }
    }
}

