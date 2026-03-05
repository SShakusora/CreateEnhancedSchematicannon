package com.sshakusora.create_enhanced_schematicannon.mixin.anvilcraft.blockentity;

import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import dev.dubhe.anvilcraft.block.entity.AdvancedComparatorBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancedComparatorBlockEntity.class)
public abstract class AdvancedComparatorBlockEntityMixin implements PartialSafeNBT {
    @Shadow protected abstract void saveAdditional(CompoundTag tag, HolderLookup.Provider registries);

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        this.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
