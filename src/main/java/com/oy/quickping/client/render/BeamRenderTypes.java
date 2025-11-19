package com.oy.quickping.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.BiFunction;

public class BeamRenderTypes extends RenderType {

    public static final BiFunction<ResourceLocation, Boolean, RenderType> BEAM = Util.memoize(
            (texture, translucency) -> {
                CompositeState builder = CompositeState.builder()
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTextureState(new TextureStateShard(texture, TriState.FALSE, false))
                        .setTransparencyState(translucency ? TRANSLUCENT_TRANSPARENCY : NO_TRANSPARENCY)
                        .setWriteMaskState(translucency ? COLOR_WRITE : COLOR_DEPTH_WRITE)
                        .createCompositeState(false);
                return create("quickping:ping_beam", DefaultVertexFormat.BLOCK, Mode.QUADS, 1536, false, true, builder);
            });


    public BeamRenderTypes(String name, VertexFormat format, Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType beam(ResourceLocation location, boolean colorFlag) {
        return BEAM.apply(location, colorFlag);
    }

}