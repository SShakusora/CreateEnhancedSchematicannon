package com.sshakusora.create_enhanced_schematicannon.mixin.anvilcraft.blockentity;

import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import dev.dubhe.anvilcraft.block.entity.BaseChuteBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;

@Mixin(BaseChuteBlockEntity.class)
public abstract class BaseChuteBlockEntityMixin implements PartialSafeNBT {
    @Shadow protected abstract void saveAdditional(CompoundTag tag, HolderLookup.Provider provider);

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        BaseChuteBlockEntity self = (BaseChuteBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        NonNullList<ItemStack> stacks = self.getFilteredItemStackHandler().getStacks();
        Collections.fill(stacks, ItemStack.EMPTY);

        this.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
