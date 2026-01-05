package com.sshakusora.create_enhanced_schematicannon.mixin.chinjufumod.block;

import com.ayutaki.chinjufumod.blocks.garden.Sudare;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchematicannonBlockEntity.class)
public class SudareFix {
    @Inject(method = "shouldIgnoreBlockState", at = @At("HEAD"), remap = false, cancellable = true)
    private void shouldIgnoreBlockState(BlockState state, BlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof Sudare) {
            if(state.getValue(Sudare.STAGE_1_3) == 2 || state.getValue(Sudare.STAGE_1_3) == 3) cir.setReturnValue(false);
        }
    }
}
