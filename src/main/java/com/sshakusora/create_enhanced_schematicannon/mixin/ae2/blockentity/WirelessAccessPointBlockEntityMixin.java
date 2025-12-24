package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.blockentity.networking.WirelessAccessPointBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(WirelessAccessPointBlockEntity.class)
public class WirelessAccessPointBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        WirelessAccessPointBlockEntity self = (WirelessAccessPointBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();
        for(int i = 0; i < self.getInternalInventory().size(); ++i) {
            ItemStack item = self.getInternalInventory().getStackInSlot(i);
            if(!item.isEmpty()) {
                consumed.add(item);
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        WirelessAccessPointBlockEntity self = (WirelessAccessPointBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        WirelessAccessPointBlockEntity wireless = new WirelessAccessPointBlockEntity(AEBlockEntities.WIRELESS_ACCESS_POINT.get(), self.getBlockPos(), self.getBlockState());
        for(int i = 0; i < self.getInternalInventory().size(); ++i) {
            ItemStack item = self.getInternalInventory().getStackInSlot(i);
            if(!item.isEmpty()) {
                wireless.getInternalInventory().insertItem(i, item, false);
            }
        }

        wireless.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
