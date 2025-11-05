package com.oy.quickping.client;

import com.oy.quickping.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ConfigColorScreen extends Screen {
    private final Screen parent;
    private final List<AbstractSliderButton> sliders = new ArrayList<>();
    private Button doneButton;

    public ConfigColorScreen(Screen parent) {
        super(Component.translatable("screen.quickping.config_color"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = 100;
        int startY = 70;
        int spacing = 25;
        // 创建红色滑块
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
        sliders.add(redSlider);

        // 创建绿色滑块
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
        sliders.add(greenSlider);

        // 创建蓝色滑块
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
        sliders.add(blueSlider);

        // 创建完成按钮
        this.doneButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            Config.SPEC.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX/2+35, startY + spacing * 4, 80, 20).build();
        this.addRenderableWidget(this.doneButton);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        drawImagePreview(guiGraphics);
    }
    private void drawImagePreview(GuiGraphics guiGraphics) {
        int centerX = this.width/4*3;
        int previewY = this.height/3;

        double red = Config.PARTICLE_RED.get();
        double green = Config.PARTICLE_GREEN.get();
        double blue = Config.PARTICLE_BLUE.get();

        int color = new Color((float)red, (float)green, (float)blue).getRGB();

        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.quickping.config_color.preview"), centerX, previewY - 20, 0xFFFFFF);

        int previewSize = 60;
        int previewX = centerX - previewSize / 2;

        float r = (float) red;
        float g = (float) green;
        float b = (float) blue;

        // 绘制带有颜色的图片预览
        guiGraphics.setColor(r, g, b, 1.0f);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath("quickping", "textures/particle/quick_ping_marker.png"),
                previewX, previewY, 0, 0, previewSize, previewSize, previewSize, previewSize
        );
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f); // 重置颜色

        String rgbText = String.format("RGB: %.0f, %.0f, %.0f", red * 255, green * 255, blue * 255);
        guiGraphics.drawCenteredString(this.font, rgbText, centerX, previewY + previewSize + 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
