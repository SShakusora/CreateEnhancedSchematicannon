package com.sshakusora.create_enhanced_schematicannon.mixin.dramaticdoors.block;

import com.fizzware.dramaticdoors.forge.blocks.TallDoorBlock;
import com.fizzware.dramaticdoors.forge.state.properties.TripleBlockPart;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchematicannonBlockEntity.class)
public class TallDoorBlockFix {
    @Inject(method = "shouldIgnoreBlockState", at = @At("HEAD"), remap = false, cancellable = true)
    private void shouldIgnoreBlockState(BlockState state, BlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (state.hasProperty(TallDoorBlock.THIRD) && (state.getValue(TallDoorBlock.THIRD) == TripleBlockPart.MIDDLE || state.getValue(TallDoorBlock.THIRD) == TripleBlockPart.UPPER)) {
            cir.setReturnValue(true);
        }
    }
}
