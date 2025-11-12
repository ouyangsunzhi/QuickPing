package com.oy.quickping;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue PARTICLE_RED = BUILDER
            .comment("Particle red color component (0.0 - 1.0)")
            .defineInRange("particleRed", 1.0, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue PARTICLE_GREEN = BUILDER
            .comment("Particle green color component (0.0 - 1.0)")
            .defineInRange("particleGreen", 1.0, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue PARTICLE_BLUE = BUILDER
            .comment("Particle blue color component (0.0 - 1.0)")
            .defineInRange("particleBlue", 1.0, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BEAM_DISTANCE = BUILDER
            .comment("Beam distance (0.0 - 100.0)")
            .defineInRange("beamDistance", 15.0, 0.0, 100.0);
    public static final ModConfigSpec.IntValue BEAM_LIFETIME = BUILDER
            .comment("Beam lifetime (1 - 10000)")
            .defineInRange("beamLifetime", 500, 1, 10000);
    public static final ModConfigSpec.DoubleValue BEAM_HEIGHT = BUILDER
            .comment("Beam height (0 - 100)")
            .defineInRange("beamHeight", 0.0, 0.0, 100.0);



    public static float red(){
        return PARTICLE_RED.get().floatValue();
    }
    public static float green(){
        return PARTICLE_GREEN.get().floatValue();
    }
    public static float blue(){
        return PARTICLE_BLUE.get().floatValue();
    }
    public static final ModConfigSpec SPEC = BUILDER.build();
}
