package com.oy.quickping;

import com.oy.quickping.network.BlockEffectPacket;
import com.oy.quickping.network.GlowEffectPacket;
import com.oy.quickping.network.PingPosPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import java.util.List;

public class Analyzer {
    public static final int GLOW_DURATION = 10;
    private static final double NORMAL_DISTANCE = 15.0;
    private static final double MAX_DISTANCE = 100.0;
    public static void analyze() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        boolean isUsingTelescope = isUsingTelescope(player);

        double detectionDistance = isUsingTelescope ? MAX_DISTANCE : NORMAL_DISTANCE;

        HitResult hitResult = getExtendedHitResult(player, detectionDistance);

        if (hitResult == null) {
            return;
        }
        applyEffects(hitResult);
    }
    private static boolean isUsingTelescope(LocalPlayer player) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        boolean hasTelescope = mainHandItem.getItem() == Items.SPYGLASS || offHandItem.getItem() == Items.SPYGLASS;

        boolean isUsingItem = minecraft.options.keyUse.isDown();

        return hasTelescope && isUsingItem;
    }

    private static HitResult getExtendedHitResult(LocalPlayer player, double maxDistance) {
        Vec3 viewVector = player.getViewVector(1.0F);

        Vec3 start = player.getEyePosition(1.0F);

        Vec3 end = start.add(viewVector.x * maxDistance, viewVector.y * maxDistance, viewVector.z * maxDistance);

        BlockHitResult blockHitResult = player.level().clip(new ClipContext(
                start, end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        double entityCheckDistance = Math.min(maxDistance, blockHitResult.getLocation().distanceTo(start));
        AABB searchBox = new AABB(start, start.add(viewVector.x * entityCheckDistance, viewVector.y * entityCheckDistance, viewVector.z * entityCheckDistance))
                .inflate(1.0);

        List<Entity> entities = player.level().getEntities(player, searchBox,
                entity -> entity != player && entity.isPickable());

        EntityHitResult entityHitResult = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(0.3); // 稍微扩大实体边界框
            net.minecraft.world.phys.Vec3 entityHit = entityBox.clip(start, end).orElse(null);

            if (entityHit != null) {
                double distance = start.distanceTo(entityHit);
                if (distance < closestDistance && distance <= entityCheckDistance) {
                    closestDistance = distance;
                    entityHitResult = new EntityHitResult(entity, entityHit);
                }
            }
        }

        if (entityHitResult != null) {
            return entityHitResult;
        } else if (blockHitResult.getType() != HitResult.Type.MISS) {
            return blockHitResult;
        }

        return null;
    }

    private static void applyEffects(HitResult hitResult) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (minecraft.level == null || player == null) return;




        switch (hitResult.getType()) {
            case ENTITY:
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                if (entity instanceof LivingEntity) {
                    if (minecraft.getConnection() != null) {
                        minecraft.getConnection().send(new GlowEffectPacket(entity.getId()));
                    }
                }
                minecraft.level.playSound(
                        player,
                        player.getOnPos(),
                        net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0f, 1.0f
                );
                break;
            case BLOCK:
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();

                if (minecraft.getConnection() != null) {
                    minecraft.getConnection().send(new BlockEffectPacket(blockPos,Config.red(),Config.green(),Config.blue()));
                }
                if (minecraft.getConnection() != null) {
                    minecraft.getConnection().send(new PingPosPacket(blockPos,Config.red(),Config.green(),Config.blue()));
                }

                minecraft.level.playSound(
                        player,
                        blockPos,
                        net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0f, 1.0f
                );
                break;
            case MISS:
                break;
        }
    }
}