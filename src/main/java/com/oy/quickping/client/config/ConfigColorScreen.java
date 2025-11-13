package com.oy.quickping.client.config;

import com.oy.quickping.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class ConfigColorScreen extends Screen {
    private final Screen parent;
    protected ConfigColorScreen(Screen parent) {
        super(Component.translatable("screen.quickping.config_color"));
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
        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.quickping.config_color"), this.width / 2, 15, 0xffffffff);
    }


    @Override
    protected void init() {
        super.init();
        int centerX = 100;
        int startY = 70;
        int spacing = 25;

        int buttonWidth = 200;
        int buttonHeight = 20;

        AbstractSliderButton redSlider = new AbstractSliderButton(centerX/2, startY, 150, 20,
                Component.literal(String.format("R: %.2f", Config.PARTICLE_RED.get())),
                Config.PARTICLE_RED.get()) {

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.format("R: %.2f", this.value)));
            }

            @Override
            protected void applyValue() {
                Config.PARTICLE_RED.set(this.value);
            }
        };
        this.addRenderableWidget(redSlider);

        AbstractSliderButton greenSlider = new AbstractSliderButton(centerX/2, startY + spacing, 150, 20,
                Component.literal(String.format("G: %.2f", Config.PARTICLE_GREEN.get())),
                Config.PARTICLE_GREEN.get()) {

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.format("G: %.2f", this.value)));
            }

            @Override
            protected void applyValue() {
                Config.PARTICLE_GREEN.set(this.value);
            }
        };
        this.addRenderableWidget(greenSlider);

        AbstractSliderButton blueSlider = new AbstractSliderButton(centerX/2, startY + spacing * 2, 150, 20,
                Component.literal(String.format("B: %.2f", Config.PARTICLE_BLUE.get())),
                Config.PARTICLE_BLUE.get()) {

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.format("B: %.2f", this.value)));
            }
            @Override
            protected void applyValue() {
                Config.PARTICLE_BLUE.set(this.value);
            }
        };
        this.addRenderableWidget(blueSlider);

        Button resetButton = Button.builder(Component.translatable("screen.quickping.config.reset"), button -> {
            Config.PARTICLE_RED.set(1.0);
            Config.PARTICLE_GREEN.set(1.0);
            Config.PARTICLE_BLUE.set(1.0);
            rebuildWidgets();
        }).bounds(centerX/2,startY + spacing * 3, 150, 20).build();
        this.addRenderableWidget(resetButton);

        Button backButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            Config.SPEC.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - buttonWidth / 2, this.height -25, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(backButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width/4*3;
        int previewY = this.height/3;

        float r = Config.PARTICLE_RED.get().floatValue();
        float g = Config.PARTICLE_GREEN.get().floatValue();
        float b = Config.PARTICLE_BLUE.get().floatValue();

        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.quickping.config_color.preview"), centerX, previewY - 20, 0xFFFFFF);

        int previewSize = 60;
        int previewX = centerX - previewSize / 2;


        guiGraphics.setColor(r, g, b, 1.0f);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath("quickping", "textures/particle/ping.png"),
                previewX, previewY, 0, 0, previewSize, previewSize, previewSize, previewSize
        );
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        String rgbText = String.format("%.0f, %.0f, %.0f", r * 255, g * 255, b * 255);
        guiGraphics.drawCenteredString(this.font, rgbText, centerX, previewY + previewSize + 10, 0xFFFFFF);

    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
