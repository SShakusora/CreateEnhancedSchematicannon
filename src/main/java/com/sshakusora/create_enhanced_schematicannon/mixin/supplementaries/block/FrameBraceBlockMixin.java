package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.block;

import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBraceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FrameBraceBlock.class)
public abstract class FrameBraceBlockMixin extends Block {
    @Shadow @Final public static BooleanProperty FLIPPED;

    public FrameBraceBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
            state = state.setValue(FLIPPED, !state.getValue(FLIPPED));
        }
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.FRONT_BACK || mirror == Mirror.LEFT_RIGHT) {
            state = state.setValue(FLIPPED, !state.getValue(FLIPPED));
        }
        return state;
    }
}
