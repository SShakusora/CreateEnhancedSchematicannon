package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.storage.DriveBlock;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.SpinRotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DriveBlock.class)
public abstract class DriveBlockMixin extends Block {
    public DriveBlockMixin(Properties properties) {
        super(properties);
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
