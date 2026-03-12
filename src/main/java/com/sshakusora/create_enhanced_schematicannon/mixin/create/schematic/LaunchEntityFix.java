package com.sshakusora.create_enhanced_schematicannon.mixin.create.schematic;

import com.simibubi.create.content.schematics.cannon.LaunchedItem;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LaunchedItem.ForEntity.class)
public class LaunchEntityFix {
    @Shadow public Entity entity;

    @Inject(method = "place", at = @At("HEAD"), remap = false)
    private void placeFix(Level world, CallbackInfo ci) {
        if (this.entity != null && this.entity instanceof IPartialSafeNBT) {
            CompoundTag tag = new CompoundTag();
            ((IPartialSafeNBT) this.entity).writeSafe(tag);
            this.entity.load(tag);
        }
    }
}