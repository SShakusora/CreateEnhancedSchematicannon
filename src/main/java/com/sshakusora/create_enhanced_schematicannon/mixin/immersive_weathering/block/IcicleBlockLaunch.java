package com.sshakusora.create_enhanced_schematicannon.mixin.immersive_weathering.block;

import com.ordana.immersive_weathering.blocks.IcicleBlock;
import com.simibubi.create.content.schematics.SchematicPrinter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchematicPrinter.class)
public class IcicleBlockLaunch {
    @Inject(method = "shouldPlaceBlock", at = @At("RETURN"), remap = false, cancellable = true)
    private void shouldPlaceBlock(Level world, SchematicPrinter.PlacementPredicate predicate, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState toReplace = world.getBlockState(pos);
        if(!(toReplace.getBlock() instanceof IcicleBlock)) return;
        cir.setReturnValue(true);
    }
}