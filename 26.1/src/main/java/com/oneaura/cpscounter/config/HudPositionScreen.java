package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounter;
import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.configlib.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class HudPositionScreen extends Screen {
	private final Screen parent;
	private final CPSConfig config;
	private boolean dragging;
	private int dragOffsetX;
	private int dragOffsetY;

	public HudPositionScreen(Screen parent) {
		super(Component.literal("Reposition CPS Counter"));
		this.parent = parent;
		this.config = OneaurasCPSCounterClient.config;
	}

	@Override
	protected void init() {
		addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
				.bounds(this.width / 2 - 50, this.height - 28, 100, 20)
				.build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
		context.fill(0, 0, this.width, this.height, 0x80000000);
		context.centeredText(
				this.font,
				Component.literal("Drag the CPS counter to reposition it"),
				this.width / 2,
				10,
				0xFFFFFF);

		if (dragging) {
			config.hudX = Math.max(0, Math.min(mouseX - dragOffsetX, this.width - 20));
			config.hudY = Math.max(0, Math.min(mouseY - dragOffsetY, this.height - 20));
		}

		OneaurasCPSCounterClient.renderCpsHud(context, Minecraft.getInstance());
		super.extractRenderState(context, mouseX, mouseY, deltaTicks);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
		if (click.button() == 0 && isOverHud(click.x(), click.y())) {
			dragging = true;
			dragOffsetX = (int) click.x() - config.hudX;
			dragOffsetY = (int) click.y() - config.hudY;
			return true;
		}
		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		if (click.button() == 0) {
			dragging = false;
			return true;
		}
		return super.mouseReleased(click);
	}

	@Override
	public void onClose() {
		ConfigManager.save(OneaurasCPSCounter.MOD_ID);
		if (this.minecraft != null) {
			this.minecraft.setScreen(parent);
		}
	}

	private boolean isOverHud(double mouseX, double mouseY) {
		String sample = "0 | 0" + config.labelText;
		int textWidth = this.font.width(sample);
		int x1 = config.hudX - 2;
		int y1 = config.hudY - 2;
		int x2 = config.hudX + textWidth + 2;
		int y2 = config.hudY + 10;
		return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
	}
}
