package com.oy.quickping.client.config;

import com.oy.quickping.QuickPing;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = QuickPing.MODID, dist = Dist.CLIENT)
public class ConfigClient {
    public ConfigClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft,screen)->
                        new ConfigMainScreen(screen));
    }
}
