package com.sshakusora.create_enhanced_schematicannon.mixin.ae2;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.storage.DriveBlockEntity;
import appeng.items.storage.BasicStorageCell;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(DriveBlockEntity.class)
public class DriveBlockEntityMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    //TODO: rotate and mirror.
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        DriveBlockEntity self = (DriveBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        for (int slot = 0; slot < self.getCellCount(); slot++) {
            ItemStack cellItem = self.getInternalInventory().getStackInSlot(slot);
            if (cellItem != null) {
                if(!cellItem.isEmpty() && cellItem.getItem() instanceof BasicStorageCell upgradeable) {
                    IUpgradeInventory upgrades = upgradeable.getUpgrades(cellItem);
                    if (!(upgrades == null)) {
                        for (int i = 0; i < upgrades.size(); i++) {
                            ItemStack upgradeStack = upgrades.getStackInSlot(i);

                            if (!upgradeStack.isEmpty()) consumed.add(upgradeStack);
                        }
                    }
                }
                consumed.add(cellItem);
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        DriveBlockEntity self = (DriveBlockEntity) (Object) this;
        CompoundTag invTag = new CompoundTag();

        for (int slot = 0; slot < self.getCellCount(); slot++) {
            ItemStack stack = self.getInternalInventory().getStackInSlot(slot);
            if (stack.isEmpty())
                continue;

            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);

            if (itemTag.contains("tag", Tag.TAG_COMPOUND)) {
                CompoundTag tag = itemTag.getCompound("tag");

                tag.remove("keys");
                tag.remove("amts");
                tag.remove("ic");

                if (tag.isEmpty()) itemTag.remove("tag");
            }

            invTag.put("item" + slot, itemTag);
        }

        if (!invTag.isEmpty()) out.put("inv", invTag);

        out.putInt("priority", self.getPriority());
    }
}
