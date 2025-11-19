package com.oy.quickping.network.packet;

import com.oy.quickping.Config;
import com.oy.quickping.particle.PingMarkerOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PingParticlesPacket(BlockPos blockPos, float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PingParticlesPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("quickping", "block_effect")
    );

    public static final StreamCodec<FriendlyByteBuf, PingParticlesPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) ->
                    buf.writeBlockPos(packet.blockPos())
                    .writeFloat(packet.red)
                    .writeFloat(packet.green)
                    .writeFloat(packet.blue),
            buf -> new PingParticlesPacket(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PingParticlesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                ServerLevel serverLevel = (ServerLevel) sender.level();
                BlockPos effectPos = packet.blockPos().above(1);
                ClientboundLevelParticlesPacket airPacket = new ClientboundLevelParticlesPacket(
                        new PingMarkerOptions(false, packet.red(), packet.green(), packet.blue()),
                        false,
                        false,
                        effectPos.getX() + 0.5,
                        effectPos.getY()+0.5,
                        effectPos.getZ() + 0.5,
                        0, 0, 0,
                        1,
                        1
                );
                int distance = Config.BEAM_DISTANCE.get();

            for (ServerPlayer player : serverLevel.players()) {
                Direction playerDirection = getClose(effectPos, player);
                ClientboundLevelParticlesPacket particlePacketToSend;

                if (playerDirection == null || serverLevel.getBlockState(effectPos).isAir()) {
                    particlePacketToSend = airPacket;
                } else {
                    double offsetX = 0.5;
                    double offsetY = -0.5;
                    double offsetZ = 0.5;

                    switch (playerDirection) {
                        case EAST -> offsetX = 1.5;
                        case WEST -> offsetX = -0.5;
                        case SOUTH -> offsetZ = 1.5;
                        case NORTH -> offsetZ = -0.5;
                    }

                    double particleX = effectPos.getX() + offsetX;
                    double particleY = effectPos.getY() + offsetY;
                    double particleZ = effectPos.getZ() + offsetZ;

                    particlePacketToSend = new ClientboundLevelParticlesPacket(
                            new PingMarkerOptions(false, packet.red(), packet.green(), packet.blue()),
                            false,
                            false,
                            particleX,
                            particleY,
                            particleZ,
                            0, 0, 0,
                            1,
                            1
                    );
                }
                player.connection.send(particlePacketToSend);
            }
        });
    }

    public static Direction getClose(BlockPos pos, Player player) {
        Vec3 blockCenter = new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        Vec3 playerPos = player.position();

        double dx = playerPos.x - blockCenter.x;
        double dz = playerPos.z - blockCenter.z;

        if (dx == 0 && dz == 0) {
            return null;
        }

        double angle = Math.atan2(dz, dx);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        if (angle < Math.PI / 4 || angle >= 7 * Math.PI / 4) {
            return Direction.EAST;
        } else if (angle < 3 * Math.PI / 4) {
            return Direction.SOUTH;
        } else if (angle < 5 * Math.PI / 4) {
            return Direction.WEST;
        } else {
            return Direction.NORTH;
        }
    }
}
