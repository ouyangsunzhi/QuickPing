package com.oy.quickping;

import com.oy.quickping.network.GlowEffectPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.*;

import java.util.List;

public class Analyzer {
    public static final int GLOW_DURATION = 10;
    private static final double NORMAL_DISTANCE = 15.0;
    private static final double MAX_DISTANCE = 100.0;
    private static final String MESSAGE_BLOCK = "message.quickping.block";
    private static final String MESSAGE_ENTITY = "message.quickping.entity";
    private static final String MESSAGE_LIVING_ENTITY = "message.quickping.living_entity";
    private static final String MESSAGE_GLOW_EFFECT_APPLIED = "message.quickping.glow_effect_applied";
    private static final String MESSAGE_BLOCK_NO_GLOW = "message.quickping.block_no_glow";
    private static final String MESSAGE_TELESCOPE_MODE = "message.quickping.telescope_mode";
    private static final String MESSAGE_NORMAL_MODE = "message.quickping.normal_mode";
    private static final String MESSAGE_PREFIX = "message.quickping.prefix";

    public static void analyzeAndSendMessage() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        boolean isUsingTelescope = isUsingTelescope(player);

        double detectionDistance = isUsingTelescope ? MAX_DISTANCE : NORMAL_DISTANCE;

        HitResult hitResult = getExtendedHitResult(player, detectionDistance);

        if (hitResult == null) {
            return;
        }

        MutableComponent message = analyzeHitResult(hitResult);
        String modeKey = isUsingTelescope ? MESSAGE_TELESCOPE_MODE : MESSAGE_NORMAL_MODE;
        MutableComponent modeComponent = Component.translatable(modeKey);

        MutableComponent fullMessage = Component.empty()
                .append(modeComponent)
                .append(" ")
                .append(message);

        sendChatMessage(fullMessage);
        applyGlowingEffect(hitResult);
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

    private static MutableComponent analyzeHitResult(HitResult hitResult) {
        return switch (hitResult.getType()) {
            case BLOCK -> analyzeBlock((BlockHitResult) hitResult);
            case ENTITY -> analyzeEntity((EntityHitResult) hitResult);
            case MISS -> null;
        };
    }

    private static MutableComponent analyzeBlock(BlockHitResult blockHitResult) {
        Minecraft minecraft = Minecraft.getInstance();
        Block block = minecraft.level.getBlockState(blockHitResult.getBlockPos()).getBlock();
        String blockName = block.getName().getString();
        double distance = blockHitResult.getLocation().distanceTo(minecraft.player.getEyePosition());

        return Component.translatable(MESSAGE_BLOCK, blockName, String.format("%.1f", distance));
    }

    private static MutableComponent analyzeEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Minecraft minecraft = Minecraft.getInstance();
        String entityName = entity.getDisplayName().getString();
        double distance = entityHitResult.getLocation().distanceTo(minecraft.player.getEyePosition());

        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            float health = livingEntity.getHealth();
            float maxHealth = livingEntity.getMaxHealth();
            return Component.translatable(MESSAGE_LIVING_ENTITY, entityName,
                    String.format("%.1f", health), String.format("%.1f", maxHealth),
                    String.format("%.1f", distance));
        } else {
            return Component.translatable(MESSAGE_ENTITY, entityName, String.format("%.1f", distance));
        }
    }

    private static void sendChatMessage(MutableComponent message) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null) {
            MutableComponent prefix = Component.translatable(MESSAGE_PREFIX);
            MutableComponent fullMessage = Component.empty()
                    .append(prefix)
                    .append(" ")
                    .append(message);
            player.sendSystemMessage(fullMessage);
        }
    }

    private static void sendChatMessage(String messageKey) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null) {
            MutableComponent prefix = Component.translatable(MESSAGE_PREFIX);
            MutableComponent message = Component.translatable(messageKey);
            MutableComponent fullMessage = Component.empty()
                    .append(prefix)
                    .append(" ")
                    .append(message);
            player.sendSystemMessage(fullMessage);
        }
    }

    private static void applyGlowingEffect(HitResult hitResult) {
        Minecraft minecraft = Minecraft.getInstance();

        switch (hitResult.getType()) {
            case ENTITY:
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                if (entity instanceof LivingEntity) {
                    if (minecraft.getConnection() != null) {
                        minecraft.getConnection().send(new GlowEffectPacket(entity.getId()));
                        sendChatMessage(Component.translatable(MESSAGE_GLOW_EFFECT_APPLIED,
                                entity.getDisplayName().getString()));
                    }
                }
                break;
            case BLOCK:
                sendChatMessage(MESSAGE_BLOCK_NO_GLOW);
                break;
            case MISS:
                break;
        }
    }
}