package com.oy.quickping.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ItemMessagePacket(ItemStack itemStack,float r,float g,float b) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ItemMessagePacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "item_message")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ItemMessagePacket::itemStack,
            ByteBufCodecs.FLOAT,
            ItemMessagePacket::r,
            ByteBufCodecs.FLOAT,
            ItemMessagePacket::g,
            ByteBufCodecs.FLOAT,
            ItemMessagePacket::b,
            ItemMessagePacket::new
    );

    public static void handle(ItemMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(()->{
            ServerPlayer sender = (ServerPlayer) context.player();
            ServerLevel serverLevel = (ServerLevel) sender.level();
            int color = (int) (0xFF * packet.r()) << 16 | (int) (0xFF * packet.g()) << 8 | (int) (0xFF * packet.b());


            Component message = Component
                    .literal("["+sender.getDisplayName().getString()+"] ")
                    .append(Component.translatable("message.quickping.item"))
                    .append(packet.itemStack.getCount()+"")
                    .append(Component.translatable("message.quickping.item.in"))
                    .append(packet.itemStack.getItem().getName(packet.itemStack))
                    .withColor(color);
            for (ServerPlayer player : serverLevel.players()){
                player.connection.send( new ClientboundSystemChatPacket(message,false));
            }
        });
    }








    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
