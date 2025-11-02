package com.oy.quickping.network;

import com.oy.quickping.Analyzer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GlowEffectPacket(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GlowEffectPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "glow_effect")
    );

    public static final StreamCodec<FriendlyByteBuf, GlowEffectPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeInt(packet.entityId()),
            buf -> new GlowEffectPacket(buf.readInt())
    );

    public GlowEffectPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GlowEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player != null && player.server != null) {
                Entity entity = player.level().getEntity(packet.entityId());
                if (entity instanceof LivingEntity livingEntity) {
                    MobEffectInstance glowEffect = new MobEffectInstance(
                            MobEffects.GLOWING,
                            Analyzer.GLOW_DURATION * 20,
                            0,
                            false,
                            false
                    );
                    livingEntity.addEffect(glowEffect);

                    //player.sendSystemMessage(Component.translatable("message.quickping.glow_effect_applied",
                    //        entity.getDisplayName().getString()));
                }
            }
        });
    }
}