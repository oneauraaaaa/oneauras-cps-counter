package com.oneaura.cpscounter.configlib.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ButtonListEntry extends AbstractConfigListEntry<Object> {
	private final Button button;
	private final Supplier<Component> labelSupplier;

	public ButtonListEntry(Component fieldName, Component buttonText, Runnable onClick) {
		this(fieldName, () -> buttonText, onClick);
	}

	public ButtonListEntry(Component fieldName, Supplier<Component> buttonTextSupplier, Runnable onClick) {
		super(fieldName, false);
		this.labelSupplier = buttonTextSupplier;
		this.button = Button.builder(buttonTextSupplier.get(), btn -> onClick.run()).bounds(0, 0, 150, 20).build();
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Optional<Object> getDefaultValue() {
		return Optional.empty();
	}

	@Override
	public void save() {
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
		super.extractRenderState(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
		this.button.setMessage(this.labelSupplier.get());
		this.button.setX(x + entryWidth - 150);
		this.button.setY(y);
		this.button.extractRenderState(context, mouseX, mouseY, delta);
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return List.of(button);
	}

	@Override
	public List<? extends NarratableEntry> narratables() {
		return List.of(button);
	}
}
