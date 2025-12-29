package com.sshakusora.create_enhanced_schematicannon.mixin.chinjufumod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.ayutaki.chinjufumod.blocks.garden.Sudare")
public class SudareMixin extends Block{
    @Unique private final IntegerProperty STAGE_1_3 = IntegerProperty.create("stage", 1, 3);
    public SudareMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            BlockPos above = pos.above();
            if (level.getBlockState(above).isAir()) {
                level.setBlock(above, state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 18);
            }
        } else if(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            if (state.getValue(STAGE_1_3) == 2 || state.getValue(STAGE_1_3) == 3) level.setBlock(pos, state, 18);
        }
    }

    @Inject(method = "m_6402_", at = @At("HEAD"), cancellable = true, remap = false)
    private void setPlacedByFix(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if(placer == null) ci.cancel();
    }
}