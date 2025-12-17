package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.misc.ChargerBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChargerBlock.class)
public abstract class ChargerBlockMixin extends Block {
    public ChargerBlockMixin(Properties p_49795_) {super(p_49795_);}

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return state.setValue(BlockStateProperties.FACING, rotation.rotate(dir));
        }
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (state.hasProperty(BlockStateProperties.FACING)) {
            Rotation r = mirror.getRotation(state.getValue(BlockStateProperties.FACING));
            return this.rotate(state, r);
        }
        return state;
    }
}