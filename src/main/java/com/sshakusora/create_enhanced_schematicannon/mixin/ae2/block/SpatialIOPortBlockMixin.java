package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.spatial.SpatialIOPortBlock;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.SpinRotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpatialIOPortBlock.class)
public abstract class SpatialIOPortBlockMixin extends Block {
    public SpatialIOPortBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return SpinRotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return SpinRotateMirror.mirror(state, mirror);
    }
}
