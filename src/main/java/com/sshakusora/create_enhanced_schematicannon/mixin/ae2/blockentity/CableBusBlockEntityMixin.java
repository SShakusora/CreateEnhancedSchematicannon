package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IPart;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.parts.automation.AnnihilationPlanePart;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.misc.InterfacePart;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(CableBusBlockEntity.class)
public abstract class CableBusBlockEntityMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        CableBusBlockEntity self = (CableBusBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();
        List<ItemStack> strictConsumed = new ArrayList<>();

        for(Direction direction : Direction.values()) {
            IPart part = self.getPart(direction);
            if(part != null) {
                if(part instanceof AnnihilationPlanePart annihilationPlanePart) {
                    annihilationPlanePart.addPartDrop(strictConsumed, false);
                    continue;
                }
                part.addPartDrop(consumed, false);
                IUpgradeInventory upgrades = null;
                if(part instanceof UpgradeablePart upgradeablePart) {
                    upgrades = upgradeablePart.getUpgrades();
                } else if (part instanceof InterfacePart interfacePart) {
                    upgrades = interfacePart.getInterfaceLogic().getUpgrades();
                } else if (part instanceof PatternProviderPart patternProviderPart) {
                    InternalInventory patterns = patternProviderPart.getLogic().getPatternInv();
                    if(!patterns.isEmpty()) {
                        for(int i = 0; i < patterns.size(); i++) {
                            ItemStack stack = patterns.getStackInSlot(i);
                            if (!stack.isEmpty()) consumed.add(new ItemStack(AEItems.BLANK_PATTERN));
                        }
                    }
                }
                if (upgrades != null) {
                    for (int i = 0; i < upgrades.size(); i++) {
                        ItemStack upgradeStack = upgrades.getStackInSlot(i);

                        if (!upgradeStack.isEmpty()) consumed.add(upgradeStack);
                    }
                }
            }
        }

        IPart cable = self.getPart(null);
        if (cable != null) {
            cable.addPartDrop(consumed, false);
        }

        ItemRequirement requirement = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
        if(!strictConsumed.isEmpty()) {
            List<ItemRequirement.StackRequirement> strictRequirement = new ArrayList<>();
            strictConsumed.forEach(s -> strictRequirement.add(new ItemRequirement.StrictNbtStackRequirement(s, ItemRequirement.ItemUseType.CONSUME)));
            requirement = requirement.union(new ItemRequirement(strictRequirement));
        }

        return requirement.isEmpty() ? ItemRequirement.NONE : requirement;
    }

    @Override
    public void writeSafe(CompoundTag out) {
        CableBusBlockEntity self = (CableBusBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        for(Direction direction : Direction.values()) {
            IPart part = self.getPart(direction);
            if(part instanceof InterfacePart interfacePart) {
                GenericStackInv storage = interfacePart.getStorage();
                storage.clear();
            } else if(part instanceof PatternProviderPart patternProviderPart) {
                GenericStackInv storage = patternProviderPart.getLogic().getReturnInv();
                storage.clear();
            }
        }

        self.saveAdditional(tag);
        out.merge(tag);
    }
}