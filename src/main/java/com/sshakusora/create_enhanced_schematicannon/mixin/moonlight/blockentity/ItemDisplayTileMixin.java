package com.sshakusora.create_enhanced_schematicannon.mixin.moonlight.blockentity;

import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemDisplayTile.class)
public class ItemDisplayTileMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        ItemDisplayTile self = (ItemDisplayTile) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();
        for(int i = 0; i < self.getContainerSize(); ++i) {
            if(self.getItem(i) != null) {
                consumed.add(self.getItem(i));
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        ItemDisplayTile self = (ItemDisplayTile) (Object) this;
        CompoundTag tag = new CompoundTag();

        for(int i = 0; i < self.getContainerSize(); ++i) {
            if(self.getItem(i) != null) {
                self.setItem(i, new ItemStack(self.getItem(i).getItem()));
            }
        }

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
