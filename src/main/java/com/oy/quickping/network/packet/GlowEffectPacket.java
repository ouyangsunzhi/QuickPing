package com.oy.quickping.network.packet;

import com.oy.quickping.Analyzer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GlowEffectPacket(int entityId, float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GlowEffectPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "glow_effect")
    );

    public static final StreamCodec<FriendlyByteBuf, GlowEffectPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.entityId());
                buf.writeFloat(packet.red());
                buf.writeFloat(packet.green());
                buf.writeFloat(packet.blue());
            },
            buf -> new GlowEffectPacket(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );

    public GlowEffectPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(GlowEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel serverLevel = (ServerLevel) player.level();
            Entity entity = serverLevel.getEntity(packet.entityId());
            if (entity instanceof LivingEntity livingEntity) {
                MobEffectInstance glowEffect = new MobEffectInstance(
                        MobEffects.GLOWING,
                        Analyzer.GLOW_DURATION * 20,
                        0,
                        true,
                        false
                );
                livingEntity.addEffect(glowEffect);
                GlowColorPacket glowColorPacket = new GlowColorPacket(
                        packet.entityId(),
                        packet.red(),
                        packet.green(),
                        packet.blue()
                );
                for (ServerPlayer p :serverLevel.players()) {
                    p.connection.send(glowColorPacket);
                }
            }
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