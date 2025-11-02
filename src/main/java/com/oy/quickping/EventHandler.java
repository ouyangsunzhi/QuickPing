package com.oy.quickping;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
public class EventHandler {
    /**
     * 处理客户端tick事件，检测按键按下
     */
    @SubscribeEvent
    public  void onClientTick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) return;

        while (KeyBindings.ANALYZE_KEY.consumeClick()) {
            Analyzer.analyzeAndSendMessage();
        }

        /*while (KeyBindings.COLOR_CYCLE_KEY.consumeClick()) {
            GlowColorConfig.cycleToNextColor();
            String colorName = GlowColorConfig.getCurrentColorName();
            String message = "发光颜色已切换为: " + colorName;
            minecraft.player.sendSystemMessage(Component.literal("[快速分析] " + message));
        }

         */
    }
}
