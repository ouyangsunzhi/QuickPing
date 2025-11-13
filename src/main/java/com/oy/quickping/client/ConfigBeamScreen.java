package com.oy.quickping.client;

import com.oy.quickping.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigBeamScreen extends Screen {
    private final Screen parent;
    public ConfigBeamScreen(Screen parent) {
        super(Component.translatable("screen.quickping.config_beam"));
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
        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.quickping.config_beam"), this.width / 2, 15, 0xffffffff);
    }


    @Override
    protected void init() {
        super.init();
        int boxX = this.width-200;
        int boxY = 30;
        int boxWidth = 150;
        int boxHeight = 20;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 30;
        EditBox distanceBox = new CustomNumberBox(this.font, boxX, boxY+spacing, boxWidth, boxHeight,0,100, Component.translatable("screen.quickping.config_beam.distance"));
        distanceBox.insertText(Config.BEAM_DISTANCE.get().toString());

        EditBox lifeBox = new CustomNumberBox(this.font, boxX, boxY+spacing*2, boxWidth, boxHeight,0,10000, Component.translatable("screen.quickping.config_beam.lifetime"));
        lifeBox.insertText(Config.BEAM_LIFETIME.get().toString());

        EditBox heightBox = new CustomNumberBox(this.font, boxX, boxY+spacing*3, boxWidth, boxHeight,0,100, Component.translatable("screen.quickping.config_beam.height"));
        heightBox.insertText(Config.BEAM_HEIGHT.get().toString());

        Button applyButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            Config.BEAM_DISTANCE.set(Integer.parseInt(distanceBox.getValue()));
            Config.BEAM_LIFETIME.set(Integer.parseInt(lifeBox.getValue()));
            Config.BEAM_HEIGHT.set(Integer.parseInt(heightBox.getValue()));
            Config.SPEC.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - buttonWidth / 2, this.height -25, buttonWidth, buttonHeight).build();

        Button resetButton = Button.builder(Component.translatable("screen.quickping.config.reset"), button -> {
            Config.BEAM_DISTANCE.set(Config.BEAM_DISTANCE.getDefault());
            Config.BEAM_LIFETIME.set(Config.BEAM_LIFETIME.getDefault());
            Config.BEAM_HEIGHT.set(Config.BEAM_HEIGHT.getDefault());
            distanceBox.setValue(Config.BEAM_DISTANCE.get().toString());
            lifeBox.setValue(Config.BEAM_LIFETIME.get().toString());
            heightBox.setValue(Config.BEAM_HEIGHT.get().toString());
            Config.SPEC.save();
        }).bounds(this.width / 2 - buttonWidth / 2, this.height -60, buttonWidth, buttonHeight).build();

        this.addRenderableWidget(distanceBox);
        this.addRenderableWidget(lifeBox);
        this.addRenderableWidget(heightBox);
        this.addRenderableWidget(applyButton);
        this.addRenderableWidget(resetButton);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int textX = 100;
        int textY = 35;
        int spacing = 30;
        guiGraphics.drawString(this.font,Component.translatable("screen.quickping.config_beam.distance"),textX,textY+spacing,0xffffff);
        guiGraphics.drawString(this.font,Component.translatable("screen.quickping.config_beam.lifetime"),textX,textY+spacing*2,0xffffff);
        guiGraphics.drawString(this.font,Component.translatable("screen.quickping.config_beam.height"),textX,textY+spacing*3,0xffffff);
    }
}
