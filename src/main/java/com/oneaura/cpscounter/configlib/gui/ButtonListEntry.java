package com.oneaura.cpscounter.configlib.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A custom Cloth Config entry that displays a label on the left and a clickable
 * button on the right.
 */
public class ButtonListEntry extends AbstractConfigListEntry<Object> {

    private final ButtonWidget buttonWidget;

    public ButtonListEntry(Text fieldName, Text buttonText, Runnable onClick) {
        super(fieldName, false);
        this.buttonWidget = ButtonWidget.builder(buttonText, btn -> onClick.run())
                .dimensions(0, 0, 150, 20)
                .build();
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
        // No value to save
    }

    @Override
    public void render(DrawContext drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
            int mouseY, boolean isHovered, float delta) {
        super.render(drawContext, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        // Position the button on the right side of the entry
        this.buttonWidget.setX(x + entryWidth - 150);
        this.buttonWidget.setY(y);

        // Render the button
        this.buttonWidget.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.singletonList(buttonWidget);
    }

    @Override
    public List<? extends Selectable> narratables() {
        return Collections.singletonList(buttonWidget);
    }
}
