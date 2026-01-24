package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.helpers.patternprovider.PatternProviderReturnInventory;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(PatternProviderBlockEntity.class)
public class PatternProviderBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        PatternProviderBlockEntity self = (PatternProviderBlockEntity) (Object)this;
        List<ItemStack> consumed = new ArrayList<>();

        InternalInventory patterns = self.getLogic().getPatternInv();
        if(!patterns.isEmpty()) {
            for(int i = 0; i < patterns.size(); i++) {
                ItemStack stack = patterns.getStackInSlot(i);
                if (!stack.isEmpty()) consumed.add(new ItemStack(AEItems.BLANK_PATTERN));
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        PatternProviderBlockEntity self = (PatternProviderBlockEntity) (Object)this;
        CompoundTag tag = new CompoundTag();

        PatternProviderReturnInventory inv = self.getLogic().getReturnInv();
        inv.clear();

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
