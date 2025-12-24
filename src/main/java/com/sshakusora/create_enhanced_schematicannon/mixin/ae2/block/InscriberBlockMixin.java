package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.misc.InscriberBlock;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.SpinRotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InscriberBlock.class)
public abstract class InscriberBlockMixin extends Block {
    public InscriberBlockMixin(Properties p_49795_) {super(p_49795_);}

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return SpinRotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return SpinRotateMirror.mirror(state, mirror);
    }
}
