package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.misc.InscriberBlockEntity;
import appeng.util.inv.AppEngInternalInventory;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(InscriberBlockEntity.class)
public class InscriberBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Final
    @Shadow private AppEngInternalInventory topItemHandler;
    @Final
    @Shadow private AppEngInternalInventory bottomItemHandler;
    @Final
    @Shadow private AppEngInternalInventory sideItemHandler;
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        InscriberBlockEntity self = (InscriberBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        IUpgradeInventory upgrades = self.getUpgrades();
        if (upgrades != null) {
            for (int i = 0; i < upgrades.size(); i++) {
                ItemStack upgradeStack = upgrades.getStackInSlot(i);

                if (!upgradeStack.isEmpty()) consumed.add(upgradeStack);
            }
        }

        consumed.add(this.topItemHandler.getStackInSlot(0));
        consumed.add(this.bottomItemHandler.getStackInSlot(0));

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        InscriberBlockEntity self = (InscriberBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        this.sideItemHandler.clear();

        self.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
