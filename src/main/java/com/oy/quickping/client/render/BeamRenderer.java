package com.oy.quickping.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.awt.*;

public class BeamRenderer {
    public static void renderBeaconBeam(
            PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation beamTexture, ResourceLocation glowTexture,
            float partialTick, float textureScale, long gameTime, float yOffset, float height,
            Color colorBot, Color colorTop, float beamRadius, float glowRadius) {

        if (height <= 0) return;

        float maxY = yOffset + height;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        float animationTime = (float) Math.floorMod(gameTime, 40) + partialTick;
        float textureOffset = -animationTime;
        float texturePhase = Mth.frac(textureOffset * 0.2F - Mth.floor(textureOffset * 0.1F));

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(animationTime * 2.25F - 45.0F));
        renderBeamPart(
                poseStack,
                bufferSource,
                beamTexture,
                colorBot,
                colorTop,
                yOffset,
                maxY,
                0.0F,
                beamRadius,
                beamRadius,
                0.0F,
                -beamRadius,
                0.0F,
                0.0F,
                -beamRadius,
                textureScale,
                texturePhase,
                height
        );
        poseStack.popPose();

        renderGlowPart(
                poseStack,
                bufferSource,
                glowTexture,
                colorBot,
                colorTop,
                yOffset,
                maxY,
                glowRadius,
                textureScale,
                texturePhase,
                height);

        poseStack.popPose();
    }

    private static void renderBeamPart(
            PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture,
            Color colorBot, Color colorTop, float minY, float maxY,
            float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
            float textureScale, float texturePhase, float height) {

        VertexConsumer consumer = bufferSource.getBuffer(BeamRenderTypes.beam(texture, true));
        float minV = -1.0F + texturePhase;
        float maxV = (height * textureScale * (0.5F / x2)) + minV;

        renderQuadPart(poseStack.last(), consumer, colorBot, colorTop, minY, maxY,
                x1, z1, x2, z2, x3, z3, x4, z4, 0.0F, 1.0F, minV, maxV);
    }

    private static void renderGlowPart(
            PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture,
            Color colorBot, Color colorTop, float minY, float maxY,
            float glowRadius, float textureScale, float texturePhase, float height) {

        Color glowBot = new Color(colorBot.getRed(), colorBot.getGreen(), colorBot.getBlue(), colorBot.getAlpha()/2);
        Color glowTop = new Color(colorTop.getRed(), colorTop.getGreen(), colorTop.getBlue(), colorTop.getAlpha()/2);

        VertexConsumer consumer = bufferSource.getBuffer(BeamRenderTypes.beam(texture, true));
        float minV = -1.0F + texturePhase;
        float maxV = (height * textureScale) + minV;

        renderQuadPart(
                poseStack.last(),
                consumer,
                glowBot,
                glowTop,
                minY,
                maxY,
                -glowRadius,
                -glowRadius,
                glowRadius,
                -glowRadius,
                -glowRadius,
                glowRadius,
                glowRadius,
                glowRadius,
                0.0F, 1.0F, minV, maxV);
    }

    private static void renderQuadPart(
            PoseStack.Pose pose, VertexConsumer consumer, Color colorBot, Color colorTop,
            float minY, float maxY, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
            float minU, float maxU, float minV, float maxV) {

        renderQuad(pose, consumer, colorBot, colorTop, minY, maxY, x1, z1, x2, z2, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, colorBot, colorTop, minY, maxY, x4, z4, x3, z3, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, colorBot, colorTop, minY, maxY, x2, z2, x4, z4, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, colorBot, colorTop, minY, maxY, x3, z3, x1, z1, minU, maxU, minV, maxV);
    }

    private static void renderQuad(
            PoseStack.Pose pose, VertexConsumer consumer, Color colorBot, Color colorTop,
            float minY, float maxY, float minX, float minZ, float maxX, float maxZ,
            float minU, float maxU, float minV, float maxV) {

        addVertex(pose, consumer, colorTop, maxY, minX, minZ, maxU, minV);
        addVertex(pose, consumer, colorBot, minY, minX, minZ, maxU, maxV);
        addVertex(pose, consumer, colorBot, minY, maxX, maxZ, minU, maxV);
        addVertex(pose, consumer, colorTop, maxY, maxX, maxZ, minU, minV);
    }

    private static void addVertex(
            PoseStack.Pose pose, VertexConsumer consumer, Color color, float y, float x, float z,
            float u, float v) {

        consumer.addVertex(pose, x, y, z)
                .setColor(color.getRGB())
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
