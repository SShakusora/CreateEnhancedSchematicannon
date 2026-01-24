package com.sshakusora.create_enhanced_schematicannon.mixin.chimes.block;

import com.nick.chimes.block.WindChimeBlock;
import com.sshakusora.create_enhanced_schematicannon.util.RotateMirror;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindChimeBlock.class)
public class WindChimeBlockMixin extends Block {
    public WindChimeBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(WindChimeBlock.HALF) == DoubleBlockHalf.LOWER) {
            BlockPos above = pos.above();
            if (level.getBlockState(above).isAir()) {
                level.setBlock(above, state.setValue(WindChimeBlock.HALF, DoubleBlockHalf.UPPER), 18);
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return RotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return RotateMirror.mirror(state, mirror);
    }

    @Inject(method = "setPlacedBy", at = @At("HEAD"), remap = false, cancellable = true)
    private void setPlacedByFix(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if(placer == null) ci.cancel();
    }
}
