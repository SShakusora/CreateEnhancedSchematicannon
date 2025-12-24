package com.sshakusora.create_enhanced_schematicannon.mixin.amendments.blockentity;

import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.mehvahdjukaar.amendments.common.tile.HangingSignTileExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(HangingSignTileExtension.class)
public class HangingSignTileExtensionMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        HangingSignTileExtension self = (HangingSignTileExtension) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();
        if(self.getBackItem() != null) {
            consumed.add(self.getBackItem());
        }
        if(self.getFrontItem() != null) {
            consumed.add(self.getFrontItem());
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        HangingSignTileExtension self = (HangingSignTileExtension) (Object) this;
        CompoundTag tag = new CompoundTag();

        if(self.getBackItem() != null) {
            self.setBackItem(new ItemStack(self.getBackItem().getItem()));
        }
        if(self.getFrontItem() != null) {
            self.setFrontItem(new ItemStack(self.getFrontItem().getItem()));
        }

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
