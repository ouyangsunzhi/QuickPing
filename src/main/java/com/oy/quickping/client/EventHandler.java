package com.oy.quickping.client;

import com.oy.quickping.Analyzer;
import com.oy.quickping.KeyBindings;
import com.oy.quickping.client.render.BeamRenderList;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@OnlyIn(Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        BeamRenderList.tick();

        if (minecraft.player == null) return;

        while (KeyBindings.ANALYZE_KEY.consumeClick()) {
            Analyzer.analyze(false);
        }
        while (KeyBindings.RADIAL_MENU_KEY.consumeClick()) {
            minecraft.setScreen(new RadialMenuScreen());
        }
    }
}