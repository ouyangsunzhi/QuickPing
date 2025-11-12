package com.oy.quickping.network;

import com.oy.quickping.particle.PingMarkerOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BlockEffectPacket(BlockPos blockPos, float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BlockEffectPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "block_effect")
    );

    public static final StreamCodec<FriendlyByteBuf, BlockEffectPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) ->
                    buf.writeBlockPos(packet.blockPos())
                    .writeFloat(packet.red)
                    .writeFloat(packet.green)
                    .writeFloat(packet.blue),
            buf -> new BlockEffectPacket(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BlockEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                ServerLevel serverLevel = (ServerLevel) sender.level();
                BlockPos effectPos = packet.blockPos().above(2);

                ClientboundLevelParticlesPacket particlePacket = new ClientboundLevelParticlesPacket(
                        new PingMarkerOptions(packet.red(), packet.green(), packet.blue()),
                        false,
                        effectPos.getX() + 0.5,
                        effectPos.getY() + 0.5,
                        effectPos.getZ() + 0.5,
                        0, 0, 0,
                        1,
                        1
                );
                for (ServerPlayer player : serverLevel.players()) {
                    player.connection.send(particlePacket);
                }
        });
    }
}
