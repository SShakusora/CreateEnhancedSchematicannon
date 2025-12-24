package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.block;

import com.sshakusora.create_enhanced_schematicannon.util.ae2.RotateMirror;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FrameBlock.class)
public abstract class FrameBlockMixin extends Block {
    public FrameBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return RotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return RotateMirror.mirror(state, mirror);
    }
}
