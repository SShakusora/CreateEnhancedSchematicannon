package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.misc.LightDetectorBlock;
import com.sshakusora.create_enhanced_schematicannon.util.RotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightDetectorBlock.class)
public abstract class LightDetectorBlockMixin extends Block {
    public LightDetectorBlockMixin(Properties properties) {
        super(properties);
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
