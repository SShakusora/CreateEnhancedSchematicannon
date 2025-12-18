package com.sshakusora.create_enhanced_schematicannon.util.ae2;

import appeng.api.orientation.IOrientationStrategy;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SpinRotateMirror {
    public static BlockState rotate(BlockState state, Rotation rotation) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        int spin = state.getValue(IOrientationStrategy.SPIN);

        Direction newFacing = rotation.rotate(facing);

        int newSpin = spin;

        if (facing == Direction.UP || facing == Direction.DOWN) {
            int steps = rotation == Rotation.NONE ? 0
                    : rotation == Rotation.CLOCKWISE_90 ? 1
                    : rotation == Rotation.CLOCKWISE_180 ? 2
                    : 3;

            newSpin = (spin + steps) & 3;
        }

        return state
                .setValue(BlockStateProperties.FACING, newFacing)
                .setValue(IOrientationStrategy.SPIN, newSpin);
    }

    public static BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        int spin = state.getValue(IOrientationStrategy.SPIN);

        Direction up = SpinMapping.getUpFromSpin(facing, spin);

        Direction newFacing = mirror.mirror(facing);
        Direction newUp = mirror.mirror(up);

        int newSpin = SpinMapping.getSpinFromUp(newFacing, newUp);

        return state
                .setValue(BlockStateProperties.FACING, newFacing)
                .setValue(IOrientationStrategy.SPIN, newSpin);
    }
}
