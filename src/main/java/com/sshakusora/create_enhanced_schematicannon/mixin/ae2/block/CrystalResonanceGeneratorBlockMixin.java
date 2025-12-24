package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.networking.CrystalResonanceGeneratorBlock;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.RotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrystalResonanceGeneratorBlock.class)
public abstract class CrystalResonanceGeneratorBlockMixin extends Block {
    public CrystalResonanceGeneratorBlockMixin(Properties p_49795_) {
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
