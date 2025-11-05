package com.oy.quickping.client;

import com.oy.quickping.QuickPing;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = QuickPing.MOD_ID, dist = Dist.CLIENT)
public class ConfigClient {
    public ConfigClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft,screen)->
                        new ConfigMainScreen(screen));
    }
}
