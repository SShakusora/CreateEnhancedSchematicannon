package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.block;

import com.buuz135.functionalstorage.block.DrawerBlock;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DrawerBlock.class)
public class DrawerBlockMixin extends Block {
    @Unique DrawerBlock self = (DrawerBlock) (Object) this;
    public DrawerBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        RotatableBlock.RotationType rotationType = self.getRotationType();
        if (rotationType == RotatableBlock.RotationType.FOUR_WAY) {
            Direction facing = state.getValue(RotatableBlock.FACING_HORIZONTAL);
            return state.setValue(RotatableBlock.FACING_HORIZONTAL, rotation.rotate(facing));
        } else if (rotationType == RotatableBlock.RotationType.SIX_WAY) {
            Direction facing = state.getValue(RotatableBlock.FACING_ALL);
            return state.setValue(RotatableBlock.FACING_HORIZONTAL, rotation.rotate(facing));
        } else if (rotationType == RotatableBlock.RotationType.TWENTY_FOUR_WAY) {
            Direction customFacing = state.getValue(RotatableBlock.FACING_ALL);
            Direction facing = state.getValue(RotatableBlock.FACING_ALL);
            if (customFacing == Direction.UP || customFacing == Direction.DOWN) {
                return state.setValue(RotatableBlock.FACING_ALL, rotation.rotate(facing));
            } else {
                return state.setValue(RotatableBlock.FACING_ALL, rotation.rotate(customFacing));
            }
        }
        return super.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        RotatableBlock.RotationType rotationType = self.getRotationType();
        if (rotationType == RotatableBlock.RotationType.FOUR_WAY) {
            Direction facing = state.getValue(RotatableBlock.FACING_HORIZONTAL);
            return state.setValue(RotatableBlock.FACING_HORIZONTAL, mirror.mirror(facing));
        } else if (rotationType == RotatableBlock.RotationType.SIX_WAY) {
            Direction facing = state.getValue(RotatableBlock.FACING_ALL);
            return state.setValue(RotatableBlock.FACING_HORIZONTAL, mirror.mirror(facing));
        } else if (rotationType == RotatableBlock.RotationType.TWENTY_FOUR_WAY) {
            Direction customFacing = state.getValue(RotatableBlock.FACING_ALL);
            Direction facing = state.getValue(RotatableBlock.FACING_ALL);
            if (customFacing == Direction.UP || customFacing == Direction.DOWN) {
                return state.setValue(RotatableBlock.FACING_ALL, mirror.mirror(facing));
            } else {
                return state.setValue(RotatableBlock.FACING_ALL, mirror.mirror(customFacing));
            }
        }
        return super.mirror(state, mirror);
    }
}
