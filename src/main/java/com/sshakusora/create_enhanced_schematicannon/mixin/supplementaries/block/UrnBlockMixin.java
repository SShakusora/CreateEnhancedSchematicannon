package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.block;

import net.mehvahdjukaar.supplementaries.common.block.blocks.UrnBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UrnBlock.class)
public class UrnBlockMixin {
    @Inject(method = "onPlace", at = @At("HEAD"))
    private void onPlaceFix(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if(state.getValue(UrnBlock.TREASURE)) {
            level.setBlock(pos, state.setValue(UrnBlock.TREASURE, false), 18);
        }
    }
}