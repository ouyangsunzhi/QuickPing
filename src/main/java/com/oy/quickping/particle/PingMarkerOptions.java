package com.oy.quickping.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public class PingMarkerOptions extends ParticleType<PingMarkerOptions> implements ParticleOptions {

    public final float red;
    public final float green;
    public final float blue;

    public PingMarkerOptions(float red, float green, float blue) {
        super(false);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public PingMarkerOptions(boolean override, float red, float green, float blue) {
        super(override);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static final MapCodec<PingMarkerOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("red").forGetter(p -> p.red),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("green").forGetter(p -> p.green),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("blue").forGetter(p -> p.blue)
            ).apply(instance, PingMarkerOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PingMarkerOptions> STREAM_CODEC =
            StreamCodec.of(
                    (buf,o)->{
                        buf.writeFloat(o.red);
                        buf.writeFloat(o.green);
                        buf.writeFloat(o.blue);
                    },
                    buf->new PingMarkerOptions(
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat()
                    )
            );



    @Override
    public ParticleType<?> getType() {
        return CustomParticleTypes.QUICK_PING_MARKER.get();
    }

    @Override
    public MapCodec<PingMarkerOptions> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, PingMarkerOptions> streamCodec() {
        return STREAM_CODEC;
    }
}
