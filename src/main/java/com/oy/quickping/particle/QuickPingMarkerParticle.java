package com.oy.quickping.particle;

import com.oy.quickping.Config;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@OnlyIn(Dist.CLIENT)
public class QuickPingMarkerParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    protected QuickPingMarkerParticle(ClientLevel level, double x, double y, double z,
                                      double xd, double yd, double zd, SpriteSet spriteSet) {
        super(level, x, y, z, xd, yd, zd);
        this.spriteSet = spriteSet;

        this.lifetime = 300;
        this.quadSize = 0.7F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.rCol = Config.PARTICLE_RED.get().floatValue();
        this.gCol = Config.PARTICLE_GREEN.get().floatValue();
        this.bCol = Config.PARTICLE_BLUE.get().floatValue();
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        if (this.age > this.lifetime - 20) {
            this.alpha = (float)(this.lifetime - this.age) / 20.0F;
        }
        
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                      double x, double y, double z,
                                      double xSpeed, double ySpeed, double zSpeed) {
           return new QuickPingMarkerParticle(level, x, y, z, 0, 0, 0, sprite);
        }
    }
    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = "quickping", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class QuickPingMarkerParticleRegistration {
        @SubscribeEvent
        public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(CustomParticleTypes.QUICK_PING_MARKER.get(), QuickPingMarkerParticle.Provider::new);
        }
    }
}