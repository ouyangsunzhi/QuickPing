package com.oy.quickping.network.packet;

import com.oy.quickping.entity.EntityColorManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GlowColorPacket(int entityId, float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GlowColorPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "glow_color")
    );

    public static final StreamCodec<FriendlyByteBuf, GlowColorPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.entityId());
                buf.writeFloat(packet.red());
                buf.writeFloat(packet.green());
                buf.writeFloat(packet.blue());
            },
            buf -> new GlowColorPacket(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );

    public GlowColorPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }



    public static void handle(GlowColorPacket packet, IPayloadContext context) {
        context.enqueueWork( ()->{
            EntityColorManager.setEntityColor(packet.entityId(), packet.red(), packet.green(), packet.blue());
        });
    }



    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(red);
        buf.writeFloat(green);
        buf.writeFloat(blue);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
