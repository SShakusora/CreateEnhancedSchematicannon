package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.block;

import com.sshakusora.create_enhanced_schematicannon.util.MapDirection;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PedestalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PedestalBlock.class)
public abstract class PedestalBlockMixin extends Block {
    public PedestalBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(PedestalBlock.AXIS, MapDirection.mapAxis(null, mirror, state.getValue(PedestalBlock.AXIS)));
    }
}
