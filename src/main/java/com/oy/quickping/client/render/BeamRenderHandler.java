package com.oy.quickping.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oy.quickping.Config;
import com.oy.quickping.QuickPing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.awt.*;

@EventBusSubscriber(modid = QuickPing.MODID,value = Dist.CLIENT)
public class BeamRenderHandler {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event){
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();


        float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        Vec3 cameraPos = event.getCamera().getPosition();
        long gameTime = mc.level.getGameTime();

        float height = 100.0F;
        float yOffset = 0.75f;


        float beamRadius = 0.08f;
        float glowRadius = 0.15f;


        for (BeamRenderList.BeamData beamData :BeamRenderList.getActiveBeams()) {
            BlockPos pos = beamData.pos();
            double distance = player.distanceToSqr(Vec3.atCenterOf(pos));
            if (distance <= Config.BEAM_DISTANCE.get() * Config.BEAM_DISTANCE.get()) return;
            poseStack.pushPose();
            poseStack.translate(
                    pos.getX() - cameraPos.x,
                    pos.getY() - cameraPos.y,
                    pos.getZ() - cameraPos.z
            );

            Color dataColor_low = new Color(beamData.red(), beamData.green(), beamData.blue(), (float) 31 /255);
            Color dataColor_max = new Color(beamData.red(), beamData.green(), beamData.blue(), (float) 159 /255);
            Color dataColor_zero = new Color(beamData.red(), beamData.green(), beamData.blue(), 0);

            ResourceLocation beamTexture = ResourceLocation.fromNamespaceAndPath(QuickPing.MODID, "textures/entity/beam.png");
            ResourceLocation glowTexture = ResourceLocation.fromNamespaceAndPath(QuickPing.MODID, "textures/entity/glow.png");



            BeamRenderer.renderBeaconBeam(
                    poseStack,
                    bufferSource,
                    beamTexture,
                    glowTexture,
                    partialTicks,
                    1.0F,
                    gameTime,
                    yOffset,
                    Math.min(height, 0.5F),
                    dataColor_zero,
                    dataColor_low,
                    beamRadius,
                    glowRadius);
            height -= 0.5F;

            BeamRenderer.renderBeaconBeam(poseStack, bufferSource, beamTexture, glowTexture,
                    partialTicks,
                    1.0F,
                    gameTime,
                    yOffset+0.5f,
                    Math.clamp(height, 0, 0.5F),
                    dataColor_low,
                    dataColor_max,
                    beamRadius,
                    glowRadius);
            height -= 0.5F;



            BeamRenderer.renderBeaconBeam(poseStack, bufferSource, beamTexture, glowTexture,
                    partialTicks,
                    1.0F,
                    gameTime,
                    yOffset+1.0f,
                    Math.clamp(height, 0, 0.5F),
                    dataColor_max,
                    dataColor_max,
                    beamRadius,
                    glowRadius);
            height -= 0.5F;

            BeamRenderer.renderBeaconBeam(poseStack, bufferSource, beamTexture, glowTexture,
                    partialTicks,
                    1.0F,
                    gameTime,
                    yOffset+1.5F,
                    Math.clamp(height, 0, Config.BEAM_HEIGHT.get()+10F),
                    dataColor_max,
                    dataColor_zero,
                    beamRadius,
                    glowRadius);
             poseStack.popPose();
        }
    }

}
