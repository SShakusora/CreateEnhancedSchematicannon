package com.sshakusora.create_enhanced_schematicannon.util.ae2;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotateMirror {
    public static BlockState rotate(BlockState state, Rotation rotation) {
        if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return state.setValue(BlockStateProperties.FACING, rotation.rotate(dir));
        }
        return state;
    }

    public static BlockState mirror(BlockState state, Mirror mirror) {
        if (state.hasProperty(BlockStateProperties.FACING)) {
            Rotation r = mirror.getRotation(state.getValue(BlockStateProperties.FACING));
            return rotate(state, r);
        }
        return state;
    }
}
