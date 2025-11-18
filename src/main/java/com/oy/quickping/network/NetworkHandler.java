package com.oy.quickping.network;

import com.oy.quickping.network.packet.*;
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
                PingParticlesPacket.TYPE,
                PingParticlesPacket.STREAM_CODEC,
                PingParticlesPacket::handle
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
        registrar.playToClient(
                GlowColorPacket.TYPE,
                GlowColorPacket.STREAM_CODEC,
                GlowColorPacket::handle
        );
        registrar.playToServer(
                EntityMessagePacket.TYPE,
                EntityMessagePacket.STREAM_CODEC,
                EntityMessagePacket::handle
        );
        registrar.playToServer(
                PosMessagePacket.TYPE,
                PosMessagePacket.STREAM_CODEC,
                PosMessagePacket::handle
        );
        registrar.playToServer(
                PlayerMessagePacket.TYPE,
                PlayerMessagePacket.STREAM_CODEC,
                PlayerMessagePacket::handle
        );
        registrar.playToServer(
                ItemMessagePacket.TYPE,
                ItemMessagePacket.STREAM_CODEC,
                ItemMessagePacket::handle
        );
        registrar.playToServer(
                BlockMessagePacket.TYPE,
                BlockMessagePacket.STREAM_CODEC,
                BlockMessagePacket::handle
        );
    }
}