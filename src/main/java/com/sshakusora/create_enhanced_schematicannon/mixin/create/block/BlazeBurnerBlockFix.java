package com.sshakusora.create_enhanced_schematicannon.mixin.create.block;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.schematics.SchematicPrinter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchematicPrinter.class)
public class BlazeBurnerBlockFix {
    @Inject(method = "shouldPlaceBlock", at = @At("RETURN"), remap = false, cancellable = true)
    private void shouldPlaceBlock(Level world, SchematicPrinter.PlacementPredicate predicate, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState toReplace = world.getBlockState(pos);
        if(!(toReplace.getBlock() instanceof BlazeBurnerBlock)) return;

        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);
        if(!(aboveState.getBlock() instanceof FluidTankBlock tank)) return;

        boolean isReplaced = cir.getReturnValue();
        FluidTankBlockEntity tankBE = tank.getBlockEntity(world, abovePos);
        if (tankBE != null) {
            FluidTankBlockEntity controllerBE = tankBE.getControllerBE();
            if (controllerBE != null && controllerBE.boiler.isActive() &&isReplaced) cir.setReturnValue(false);
        }
    }
}
