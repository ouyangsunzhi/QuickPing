package com.oy.quickping.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EntityMessagePacket(BlockPos pos, String name, float r, float g, float b) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EntityMessagePacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "entity_message")
    );
    public static final StreamCodec<ByteBuf, EntityMessagePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EntityMessagePacket::pos,
            ByteBufCodecs.STRING_UTF8,
            EntityMessagePacket::name,
            ByteBufCodecs.FLOAT,
            EntityMessagePacket::r,
            ByteBufCodecs.FLOAT,
            EntityMessagePacket::g,
            ByteBufCodecs.FLOAT,
            EntityMessagePacket::b,
            EntityMessagePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(EntityMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(()->{
            ServerPlayer sender = (ServerPlayer) context.player();
            ServerLevel serverLevel = (ServerLevel) sender.level();
            int color = (int) (0xFF * packet.r()) << 16 | (int) (0xFF * packet.g()) << 8 | (int) (0xFF * packet.b());


            Component message = Component
                    .literal("["+sender.getDisplayName().getString()+"] ")
                    .append(Component.translatable("message.quickping.entity"))
                    .append(packet.name)
                    .append(Component.translatable("message.quickping.entity.in"))
                    .append(Component.literal(packet.pos().toShortString()))
                    .withColor(color);
            for (ServerPlayer player : serverLevel.players()){
                player.connection.send( new ClientboundSystemChatPacket(message,false));
            }
        });
    }
}
