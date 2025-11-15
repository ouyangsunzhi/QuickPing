package com.oy.quickping.mixin;

import com.oy.quickping.Config;
import com.oy.quickping.entity.EntityColorManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin {
    @Shadow
    public abstract int getId();

    @Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
    public void getTeamColor(CallbackInfoReturnable<Integer> cir) {
        int color = cir.getReturnValueI();
        int id = this.getId();
        if (color == 16777215 && EntityColorManager.getEntityColor(id) != -1) {
            color = EntityColorManager.getEntityColor(id);
        }
        cir.setReturnValue(color);
    }

}