package com.oy.quickping.client.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class CustomNumberBox extends EditBox {
    private final int minValue;
    private final int maxValue;


    public CustomNumberBox(Font font, int x, int y, int width, int height,int minValue,int maxValue, Component message) {
        super(font, x, y, width, height, message);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean isFunctionKey = keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE ||
                (keyCode >= GLFW.GLFW_KEY_LEFT && keyCode <= GLFW.GLFW_KEY_DOWN) ||
                keyCode == GLFW.GLFW_KEY_HOME || keyCode == GLFW.GLFW_KEY_END ||
                (modifiers != 0 && (keyCode == GLFW.GLFW_KEY_C || keyCode == GLFW.GLFW_KEY_V ||
                        keyCode == GLFW.GLFW_KEY_X || keyCode == GLFW.GLFW_KEY_A));

        if (isFunctionKey) {
            boolean result = super.keyPressed(keyCode, scanCode, modifiers);
            this.validateAndClampValue();
            return result;
        }
        return false;
    }
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (Character.isDigit(codePoint)) {
            boolean result = super.charTyped(codePoint, modifiers);
            this.validateAndClampValue();
            return result;
        }
        return false;
    }

    private void validateAndClampValue() {
        String currentText = this.getValue();

        try {
            int value = Integer.parseInt(currentText);

            if (value < minValue) {
                this.setValue(String.valueOf(minValue));
            } else if (value > maxValue) {
                this.setValue(String.valueOf(maxValue));
            }
        } catch (NumberFormatException e) {
            this.setValue("");
        }
    }


}
