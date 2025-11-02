package com.oy.quickping.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                GlowEffectPacket.TYPE,
                GlowEffectPacket.STREAM_CODEC,
                GlowEffectPacket::handle
        );
    }
}
