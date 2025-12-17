package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.util.inv.AppEngInternalInventory;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(MolecularAssemblerBlockEntity.class)
public class MolecularAssemblerBlockEntityMixin implements ISpecialBlockEntityItemRequirement,IPartialSafeNBT {
    @Final
    @Shadow private AppEngInternalInventory gridInv;

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        MolecularAssemblerBlockEntity self = (MolecularAssemblerBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        if (!self.acceptsPlans()) {
            consumed.add(new ItemStack(AEItems.BLANK_PATTERN));
        }

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
        MolecularAssemblerBlockEntity self = (MolecularAssemblerBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        this.gridInv.clear();

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
