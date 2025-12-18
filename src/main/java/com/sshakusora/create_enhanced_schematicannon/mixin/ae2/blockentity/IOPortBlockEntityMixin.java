package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.storage.IOPortBlockEntity;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(IOPortBlockEntity.class)
public class IOPortBlockEntityMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        IOPortBlockEntity self = (IOPortBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        IUpgradeInventory upgrades = self.getUpgrades();
        if (upgrades != null) {
            for (int i = 0; i < upgrades.size(); i++) {
                ItemStack upgradeStack = upgrades.getStackInSlot(i);

                if (!upgradeStack.isEmpty()) consumed.add(upgradeStack);
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        IOPortBlockEntity self = (IOPortBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        self.getInternalInventory().clear();

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
