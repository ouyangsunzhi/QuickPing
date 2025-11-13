package com.oy.quickping.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PingPosPacket(BlockPos pos,float red, float green, float blue) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PingPosPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "ping_pos")
    );

    public static final StreamCodec<ByteBuf, PingPosPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PingPosPacket::pos,
            ByteBufCodecs.FLOAT,
            PingPosPacket::red,
            ByteBufCodecs.FLOAT,
            PingPosPacket::green,
            ByteBufCodecs.FLOAT,
            PingPosPacket::blue,
            PingPosPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PingPosPacket packet, IPayloadContext context) {
        context.enqueueWork(()->{
            ServerPlayer sender = (ServerPlayer) context.player();
            ServerLevel serverLevel = (ServerLevel) sender.level();

            BeamRenderPacket broadcastPacket = new BeamRenderPacket(
                    packet.pos(),
                    packet.red(), packet.green(), packet.blue()
            );

            for (ServerPlayer player : serverLevel.players()) {
                player.connection.send(broadcastPacket);
            }
        });
    }
}
