package com.oy.quickping.client;

import com.oy.quickping.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigMainScreen extends Screen {
    private final Screen parent;
    public ConfigMainScreen(Screen parent) {
        super(Component.literal("Quick Ping Mod Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;

        Button configButton = Button.builder(
                Component.translatable("screen.quickping.config_color"),
                button -> this.minecraft.setScreen(new ConfigColorScreen(this))
        ).bounds(centerX - buttonWidth / 2, centerY - 30, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(configButton);

        Button backButton = Button.builder(
                Component.translatable("screen.quickping.back"),
                button -> this.minecraft.setScreen(this.parent)
        ).bounds(centerX - buttonWidth / 2, this.height -28, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(backButton);
    }
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}