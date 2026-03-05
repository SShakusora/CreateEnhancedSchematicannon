package com.sshakusora.create_enhanced_schematicannon.mixin.anvilcraft.block;

import dev.dubhe.anvilcraft.block.PulseGeneratorBlock;
import dev.dubhe.anvilcraft.block.entity.PulseGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(PulseGeneratorBlock.class)
public abstract class PulseGeneratorBlockMixin extends Block {
    @Shadow protected abstract void startOutputting(Level level, BlockPos pos, Supplier<BlockState> stateGetter, PulseGeneratorBlockEntity generator);

    public PulseGeneratorBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (placer == null) this.startOutputting(level, pos, () -> state, (PulseGeneratorBlockEntity) level.getBlockEntity(pos));
    }
}
