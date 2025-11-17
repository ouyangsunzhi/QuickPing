package com.oy.quickping;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final KeyMapping ANALYZE_KEY = new KeyMapping(
            "key.quickping.analyze",
            GLFW.GLFW_KEY_G,
            "key.quickping.analyzer"
    );
    public static final KeyMapping RADIAL_MENU_KEY = new KeyMapping(
            "screen.quickping.radial_menu",
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.quickping.analyzer"
    );
}
