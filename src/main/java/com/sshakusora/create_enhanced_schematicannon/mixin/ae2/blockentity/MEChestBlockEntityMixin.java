package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.blockentity.storage.MEChestBlockEntity;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(MEChestBlockEntity.class)
public class MEChestBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        MEChestBlockEntity self = (MEChestBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        ItemStack cell = self.getCell();
        if(cell != null) {
            consumed.add(cell);
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        MEChestBlockEntity self = (MEChestBlockEntity) (Object) this;
        Set<String> keysToRemove = Set.of("keys", "ic", "amts");
        CompoundTag tag = new CompoundTag();
            self.saveAdditional(tag, provider);

        if (tag.contains("inv", Tag.TAG_COMPOUND)) {
            CompoundTag inv = tag.getCompound("inv");

            for (String itemKey : inv.getAllKeys()) {
                CompoundTag item = inv.getCompound(itemKey);

                if (item.contains("tag", Tag.TAG_COMPOUND)) {
                    CompoundTag itemTag = item.getCompound("tag");

                    for (String removeKey : keysToRemove) {
                        itemTag.remove(removeKey);
                    }
                }
            }
        }

        out.merge(tag);
    }
}
