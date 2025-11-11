package com.oy.quickping.particle;

import com.mojang.serialization.MapCodec;
import com.oy.quickping.Config;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CustomParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
        DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "quickping");
//    public static final Supplier<SimpleParticleType> QUICK_PING_MARKER =
//        PARTICLE_TYPES.register("quick_ping_marker", () ->
//            new SimpleParticleType(true)
//        );
    public static final Supplier<PingMarkerOptions> QUICK_PING_MARKER =
        PARTICLE_TYPES.register("quick_ping_marker",()-> new PingMarkerOptions(0, 0, 0));
}
