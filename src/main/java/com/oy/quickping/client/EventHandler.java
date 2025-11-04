package com.oy.quickping.client;

import com.oy.quickping.Analyzer;
import com.oy.quickping.KeyBindings;
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

        if (minecraft.player == null) return;

        while (KeyBindings.ANALYZE_KEY.consumeClick()) {
            Analyzer.analyze();
        }
    }
}