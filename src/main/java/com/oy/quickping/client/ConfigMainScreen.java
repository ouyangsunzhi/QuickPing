package com.oy.quickping.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigMainScreen extends Screen {
    private final Screen parent;
    public ConfigMainScreen(Screen parent) {
        super(Component.translatable("screen.quickping.config_main"));
        this.parent = parent;
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(0, 30, this.width , this.height - 30, 0x50000000);
        guiGraphics.fill(0, 30, this.width , 31, 0x40ffffff);
        guiGraphics.fill(0, this.height - 28, this.width , this.height - 29, 0x40ffffff);
        guiGraphics.fill(0,31,this.width,32,0xff000000);
        guiGraphics.fill(0,this.height-29,this.width,this.height-30,0xff000000);
        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.quickping.config_main"), this.width/2, 15, 0xffffff);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;

        Button configButton = Button.builder(
                Component.translatable("screen.quickping.config_color"),
                button -> this.minecraft.setScreen(new ConfigColorScreen(this))
        ).bounds(this.width / 2 - buttonWidth / 2, 50 - buttonHeight / 2, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(configButton);

        Button beamButton = Button.builder(
                Component.translatable("screen.quickping.config_beam"),
                button -> this.minecraft.setScreen(new ConfigBeamScreen(this))
        ).bounds(this.width / 2 - buttonWidth / 2, 80 - buttonHeight / 2, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(beamButton);

        Button backButton = Button.builder(
                Component.translatable("screen.quickping.back"),
                button -> this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 - buttonWidth / 2, this.height -25, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(backButton);
    }
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}