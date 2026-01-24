package com.sshakusora.create_enhanced_schematicannon.mixin.quark.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.violetmoon.quark.content.building.block.GrateBlock;

@Mixin(GrateBlock.class)
public class GrateBlockMixin extends Block {
    public GrateBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if(placer == null) {
            if(state.getValue(GrateBlock.LAVALOGGED)) {
                worldIn.setBlock(pos, state.setValue(GrateBlock.LAVALOGGED, false), 18);
            }
        }
    }
}
