package com.oy.quickping.network;

import com.oy.quickping.client.BeamRenderList;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BeamRenderPacket(BlockPos pos, float red, float green, float blue) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BeamRenderPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "beam_render")
    );
    public static final StreamCodec<ByteBuf, BeamRenderPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BeamRenderPacket::pos,
            ByteBufCodecs.FLOAT,
            BeamRenderPacket::red,
            ByteBufCodecs.FLOAT,
            BeamRenderPacket::green,
            ByteBufCodecs.FLOAT,
            BeamRenderPacket::blue,
            BeamRenderPacket::new
    );



    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static void handle(BeamRenderPacket packet, IPayloadContext context) {
        context.enqueueWork(()-> BeamRenderList.addBeam(packet.pos(), packet.red(), packet.green(), packet.blue()));
    }
}
