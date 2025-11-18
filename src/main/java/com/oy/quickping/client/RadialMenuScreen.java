package com.oy.quickping.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oy.quickping.Analyzer;
import com.oy.quickping.Config;
import com.oy.quickping.KeyBindings;
import com.oy.quickping.network.packet.PingParticlesPacket;
import com.oy.quickping.network.packet.BlockMessagePacket;
import com.oy.quickping.network.packet.ItemMessagePacket;
import com.oy.quickping.network.packet.PlayerMessagePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
/**
 * 这个轮盘其实可以自定义的有很多，比如SEGMENT_COUNT，我测试的时候最大调到12都不会影响功能，只是字看不见了。
 * 没写成自定义完全是因为配置菜单不会写（详见ConfigBeamScreen.class）
 */
public class RadialMenuScreen extends Screen {
    private static final int SEGMENT_COUNT = 4;
    private static final double MENU_RADIUS = 50.0;
    private static final double INNER_RADIUS = 10.0;
    private static final double SEGMENT_ANGLE = (2 * Math.PI) / SEGMENT_COUNT;

    private final Component[] options = {
            Component.translatable("radial.menu.option1"),
            Component.translatable("radial.menu.option2"),
            Component.translatable("radial.menu.option3"),
            Component.translatable("radial.menu.option4")
    };
    private int centerX;
    private int centerY;
    private int selectedSegment = -1;
    private boolean isMouseInsideInnerCircle = false;


    public RadialMenuScreen() {
        super(Component.translatable("screen.quickping.radial_menu"));
    }

    @Override
    protected void init() {
        super.init();
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        updateSelectedSegment(mouseX, mouseY);

        drawRadialMenu(guiGraphics);

        drawOptionTexts(guiGraphics);
    }

    private void drawRadialMenu(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            int color;
            if (i == selectedSegment) {
                color = 0x80FFFFFF;
            } else {
                color = 0x40000000;
            }

            drawSegment(guiGraphics, i, color);
        }

        poseStack.popPose();
    }

    private void drawSegment(GuiGraphics guiGraphics, int segmentIndex, int color) {
        double startAngle = segmentIndex * SEGMENT_ANGLE - Math.PI / 2 - SEGMENT_ANGLE / 2;
        double endAngle = (segmentIndex + 1) * SEGMENT_ANGLE - Math.PI / 2 - SEGMENT_ANGLE / 2;

        drawArc(guiGraphics, centerX, centerY, MENU_RADIUS, startAngle, endAngle, color);
        if (isMouseInsideInnerCircle){
            drawArc(guiGraphics, centerX, centerY, INNER_RADIUS, endAngle, startAngle, 0X80FFFFFF);
        }else {
            drawArc(guiGraphics, centerX, centerY, INNER_RADIUS, endAngle, startAngle, color);
        }

        double startX1 = centerX + INNER_RADIUS * Math.cos(startAngle);
        double startY1 = centerY + INNER_RADIUS * Math.sin(startAngle);
        double endX1 = centerX + MENU_RADIUS * Math.cos(startAngle);
        double endY1 = centerY + MENU_RADIUS * Math.sin(startAngle);

        double startX2 = centerX + MENU_RADIUS * Math.cos(endAngle);
        double startY2 = centerY + MENU_RADIUS * Math.sin(endAngle);
        double endX2 = centerX + INNER_RADIUS * Math.cos(endAngle);
        double endY2 = centerY + INNER_RADIUS * Math.sin(endAngle);

        drawLine(guiGraphics, (int) startX1, (int) startY1, (int) endX1, (int) endY1, color);
        drawLine(guiGraphics, (int) startX2, (int) startY2, (int) endX2, (int) endY2, color);

    }

    private void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {
            guiGraphics.fill(x, y, x + 1, y + 1, color);

            if (x == x2 && y == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }

    private void drawArc(GuiGraphics guiGraphics, int cx, int cy, double radius, double startAngle, double endAngle, int color) {
        int segments = 20;
        double angleStep = (endAngle - startAngle) / segments;

        for (int i = 0; i < segments; i++) {
            double angle1 = startAngle + i * angleStep;
            double angle2 = startAngle + (i + 1) * angleStep;

            int x1 = (int) (cx + radius * Math.cos(angle1));
            int y1 = (int) (cy + radius * Math.sin(angle1));
            int x2 = (int) (cx + radius * Math.cos(angle2));
            int y2 = (int) (cy + radius * Math.sin(angle2));

            drawLine(guiGraphics, x1, y1, x2, y2, color);
        }
    }

    private void drawOptionTexts(GuiGraphics guiGraphics) {
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            double angle = i * SEGMENT_ANGLE - Math.PI / 2;
            double textX = centerX + (INNER_RADIUS + (MENU_RADIUS - INNER_RADIUS) / 2) * Math.cos(angle);
            double textY = centerY + (INNER_RADIUS + (MENU_RADIUS - INNER_RADIUS) / 2) * Math.sin(angle);

            int textColor;
            if (isMouseInsideInnerCircle) {
                textColor = 0xFFCCCCCC;
            } else {
                textColor = i == selectedSegment ? 0xFFFF0000 : 0xFFFFFFFF;
            }
            guiGraphics.drawCenteredString(this.font, options[i], (int) textX, (int) textY - 4, textColor);
        }
    }

    private void updateSelectedSegment(int mouseX, int mouseY) {
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        isMouseInsideInnerCircle = distance < INNER_RADIUS;

        if (isMouseInsideInnerCircle) {
            selectedSegment = -1;
            return;
        }
        double angle = Math.atan2(dy, dx);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        angle += Math.PI / 2 + SEGMENT_ANGLE / 2;
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

        selectedSegment = (int) (angle / SEGMENT_ANGLE);
        if (selectedSegment >= SEGMENT_COUNT) {
            selectedSegment = SEGMENT_COUNT - 1;
        }
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {

        if (keyCode == KeyBindings.RADIAL_MENU_KEY.getKey().getValue()) {
            if (selectedSegment >= 0) {
                executeOption(selectedSegment);
            }
            this.onClose();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    private void executeOption(int optionIndex) {
        switch (optionIndex) {
            case 0:
                handleOption1();
                break;
            case 1:
                handleOption2();
                break;
            case 2:
                handleOption3();
                break;
            case 3:
                handleOption4();
                break;
        }
    }

    private void handleOption1() {
        Analyzer.analyze(true);
    }

    private void handleOption2() {
        if (minecraft.player.getMainHandItem().isEmpty()){
            minecraft.player.sendSystemMessage(Component.translatable("message.quickping.no_item"));
            return;
        }
        minecraft.getConnection().send(new ItemMessagePacket(minecraft.player.getMainHandItem(),Config.red(),Config.green(),Config.blue()));
    }

    private void handleOption3() {
        minecraft.getConnection().send(new PlayerMessagePacket(minecraft.player.getOnPos(),Config.red(),Config.green(),Config.blue()));
    }

    private void handleOption4() {
        BlockPos pos = Analyzer.analyze();
        if (pos == null){
            minecraft.player.sendSystemMessage(Component.translatable("message.quickping.no_block"));
            return;
        }
        minecraft.getConnection().send(new BlockMessagePacket(pos,Config.red(),Config.green(),Config.blue()));
        minecraft.getConnection().send(new PingParticlesPacket(pos,Config.red(),Config.green(),Config.blue()));
    }


    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
