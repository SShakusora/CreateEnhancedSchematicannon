package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.misc.InterfaceBlockEntity;
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

@Mixin(InterfaceBlockEntity.class)
public class InterfaceBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        InterfaceBlockEntity self = (InterfaceBlockEntity) (Object) this;

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
        InterfaceBlockEntity self = (InterfaceBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        self.getInterfaceLogic().getStorage().clear();

        self.saveAdditional(tag, provider);
        out.merge(tag);
    }
}
