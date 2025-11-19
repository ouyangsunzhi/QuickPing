package com.oy.quickping.particle;

import com.oy.quickping.QuickPing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PingMarkerParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    protected PingMarkerParticle(ClientLevel level, double x, double y, double z,
                                 double xd, double yd, double zd, SpriteSet spriteSet,
                                 float r, float g, float b) {
        super(level, x, y, z, xd, yd, zd);
        this.spriteSet = spriteSet;
        this.lifetime = 300;
        this.quadSize = 0.7F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.setSpriteFromAge(spriteSet);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        if (this.age > this.lifetime - 20) {
            this.alpha = (this.lifetime - this.age) / 20.0F;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<PingMarkerOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(PingMarkerOptions options, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
           return new PingMarkerParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite,
                                         options.red, options.green, options.blue);
        }
    }
    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = QuickPing.MODID, value = Dist.CLIENT)
    static class QuickPingMarkerParticleRegistration {
        @SubscribeEvent
        public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(CustomParticleTypes.QUICK_PING_MARKER.get(), PingMarkerParticle.Provider::new);
        }
    }
}