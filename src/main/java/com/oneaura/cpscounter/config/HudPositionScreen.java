package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.configlib.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * A full-screen overlay that lets the user drag the CPS counter to any
 * position.
 * Uses GLFW polling for mouse state to avoid MC version-specific mouse event
 * API differences.
 */
public class HudPositionScreen extends Screen {

    private final Screen parent;
    private final CPSConfig config;

    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudPositionScreen(Screen parent) {
        super(Text.literal("Reposition CPS Counter"));
        this.parent = parent;
        this.config = OneaurasCPSCounterClient.config;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
            ConfigManager.save(OneaurasCPSCounterClient.MOD_ID);
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        // Semi-transparent dark overlay
        drawContext.fill(0, 0, this.width, this.height, 0x80000000);

        // Instruction text
        drawContext.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Drag the CPS counter to reposition it"),
                this.width / 2,
                10,
                0xFFFFFF);

        // Handle dragging via GLFW mouse state polling
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        boolean mouseDown = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        if (mouseDown) {
            if (!dragging) {
                // Check if mouseDown started on the HUD element
                int padding = config.padding;
                int hx = config.hudX + padding;
                int hy = config.hudY + padding;

                String sampleText = "0 | 0" + config.labelText;
                int tw = this.textRenderer.getWidth(sampleText);
                int th = 10;

                int x1 = hx - 2;
                int y1 = hy - 2;
                int x2 = hx + tw + 2;
                int y2 = hy + th;

                if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {
                    dragging = true;
                    dragOffsetX = mouseX - config.hudX;
                    dragOffsetY = mouseY - config.hudY;
                }
            } else {
                // Continue dragging
                config.hudX = Math.max(0, Math.min(mouseX - dragOffsetX, this.width - 20));
                config.hudY = Math.max(0, Math.min(mouseY - dragOffsetY, this.height - 20));
            }
        } else {
            dragging = false;
        }

        // Render the actual CPS HUD at the current config position (live preview)
        OneaurasCPSCounterClient.renderCpsHud(drawContext, MinecraftClient.getInstance());

        // Draw widgets (Done button)
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        ConfigManager.save(OneaurasCPSCounterClient.MOD_ID);
        this.client.setScreen(parent);
    }
}
