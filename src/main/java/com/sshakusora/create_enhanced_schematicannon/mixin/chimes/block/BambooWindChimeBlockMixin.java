package com.sshakusora.create_enhanced_schematicannon.mixin.chimes.block;

import com.nick.chimes.block.BambooWindChimeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooWindChimeBlock.class)
public class BambooWindChimeBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("HEAD"), remap = false, cancellable = true)
    private void setPlacedByFix(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if(placer == null) ci.cancel();
    }
}