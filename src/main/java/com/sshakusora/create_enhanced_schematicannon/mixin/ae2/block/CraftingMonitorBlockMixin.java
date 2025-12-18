package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.crafting.CraftingMonitorBlock;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.SpinRotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftingMonitorBlock.class)
public abstract class CraftingMonitorBlockMixin extends Block {
    public CraftingMonitorBlockMixin(Properties properties) {super(properties);}

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return SpinRotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return SpinRotateMirror.mirror(state, mirror);
    }
}
