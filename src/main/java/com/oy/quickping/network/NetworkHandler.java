package com.oy.quickping.network;

import com.oy.quickping.network.packet.BeamRenderPacket;
import com.oy.quickping.network.packet.BlockEffectPacket;
import com.oy.quickping.network.packet.GlowEffectPacket;
import com.oy.quickping.network.packet.PingPosPacket;
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

        registrar.playToServer(
                BlockEffectPacket.TYPE,
                BlockEffectPacket.STREAM_CODEC,
                BlockEffectPacket::handle
        );

        registrar.playToClient(
                BeamRenderPacket.TYPE,
                BeamRenderPacket.STREAM_CODEC,
                BeamRenderPacket::handle
        );
        registrar.playToServer(
                PingPosPacket.TYPE,
                PingPosPacket.STREAM_CODEC,
                PingPosPacket::handle
        );
    }
}