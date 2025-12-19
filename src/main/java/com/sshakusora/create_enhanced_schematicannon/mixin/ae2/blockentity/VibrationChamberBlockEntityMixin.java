package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.misc.VibrationChamberBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(VibrationChamberBlockEntity.class)
public class VibrationChamberBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Shadow private void setRemainingFuelTicks(double remainingFuelTicks){}
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        VibrationChamberBlockEntity self = (VibrationChamberBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        IUpgradeInventory upgrades = self.getUpgrades();
        if (!(upgrades == null)) {
            for (int i = 0; i < upgrades.size(); i++) {
                ItemStack upgradeStack = upgrades.getStackInSlot(i);

                if (!upgradeStack.isEmpty()) consumed.add(upgradeStack);
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        VibrationChamberBlockEntity self = (VibrationChamberBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        self.getInternalInventory().clear();
        this.setRemainingFuelTicks(0.0D);
        VibrationChamberBlockEntity vibrationChamber = new VibrationChamberBlockEntity(AEBlockEntities.VIBRATION_CHAMBER.get(), self.getBlockPos(), self.getBlockState());
        IUpgradeInventory upgrades = self.getUpgrades();
        if (!(upgrades == null)) {
            for (int i = 0; i < upgrades.size(); i++) {
                ItemStack upgradeStack = upgrades.getStackInSlot(i);

                if (!upgradeStack.isEmpty()) vibrationChamber.getUpgrades().insertItem(i, upgradeStack, false);
            }
        }

        vibrationChamber.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
