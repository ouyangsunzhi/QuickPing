package com.oy.quickping;

import net.neoforged.neoforge.common.ModConfigSpec;

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

    public static final ModConfigSpec.IntValue BEAM_DISTANCE = BUILDER
            .comment("Beam distance (0 - 100)")
            .defineInRange("beamDistance", 7, 0, 100);
    public static final ModConfigSpec.IntValue BEAM_LIFETIME = BUILDER
            .comment("Beam lifetime (0 - 10000)")
            .defineInRange("beamLifetime", 500, 0, 10000);
    public static final ModConfigSpec.IntValue BEAM_HEIGHT = BUILDER
            .comment("Beam height (0 - 100)")
            .defineInRange("beamHeight", 0, 0, 100);



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
