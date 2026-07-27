package com.sshakusora.create_enhanced_schematicannon.mixin.dramaticdoors.block;

import com.fizzware.dramaticdoors.neoforge.blocks.TallDoorBlock;
import com.fizzware.dramaticdoors.neoforge.state.properties.TripleBlockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TallDoorBlock.class)
public class TallDoorBlockMixin extends Block {
    public TallDoorBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(TallDoorBlock.THIRD) == TripleBlockPart.LOWER) {
            BlockPos above = pos.above();
            BlockPos above2 = pos.above(2);
            if (level.getBlockState(above).isAir()) {
                level.setBlock(above, state.setValue(TallDoorBlock.THIRD, TripleBlockPart.MIDDLE), 18);
            }
            if (level.getBlockState(above2).isAir()) {
                level.setBlock(above2, state.setValue(TallDoorBlock.THIRD, TripleBlockPart.UPPER), 18);
            }
        }
    }

    @Inject(method = "setPlacedBy", at = @At("HEAD"), remap = false, cancellable = true)
    private void setPlacedByFix(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (placer == null) ci.cancel();
    }
}
