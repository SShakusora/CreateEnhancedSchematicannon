package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IFacadePart;
import appeng.api.parts.IPart;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.misc.InterfacePart;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusContainerAccessor;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusStorageAccessor;
import net.minecraft.core.BlockPos;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(CableBusBlockEntity.class)
public class CableBusBlockEntityMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT, ITransformableBlockEntity {
public class CableBusBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        CableBusBlockEntity self = (CableBusBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();
        List<ItemStack> strictConsumed = new ArrayList<>();

        for(Direction direction : Direction.values()) {
            IPart part = self.getPart(direction);
            if(part != null) {
//                if(part instanceof AnnihilationPlanePart annihilationPlanePart) {
//                    annihilationPlanePart.addPartDrop(strictConsumed, false);
//                    continue;
//                }
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

            IFacadePart iFacadePart = self.getFacadeContainer().getFacade(direction);
            if (iFacadePart != null && iFacadePart.getTextureItem() != null) {
                //can't add facade part with nbt
                consumed.add(iFacadePart.getTextureItem());
                consumed.add(AEParts.CABLE_ANCHOR.stack());
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

        for (Direction dir : Direction.values()) {
            IPart part = self.getPart(dir);
            if (part != null) {
                //Processing special part
                if(part instanceof InterfacePart interfacePart) {
                    GenericStackInv storage = interfacePart.getStorage();
                    storage.clear();
                } else if(part instanceof PatternProviderPart patternProviderPart) {
                    GenericStackInv storage = patternProviderPart.getLogic().getReturnInv();
                    storage.clear();
                } else if(part instanceof AnnihilationPlanePart annihilationPlanePart) {
                    annihilationPlanePart.readFromNBT(new CompoundTag());
                }
            }
        }

        self.saveAdditional(tag);
        out.merge(tag);
    }

    @Override
    public void transform(StructureTransform transform) {
        CableBusBlockEntity self = (CableBusBlockEntity) (Object) this;

        CompoundTag tag = new CompoundTag();
        CableBusBlockEntity newCable = new CableBusBlockEntity(AEBlockEntities.CABLE_BUS, BlockPos.ZERO, self.getBlockState());
        CableBusContainer newCb = newCable.getCableBus();
        CableBusContainerAccessor newCbAc = (CableBusContainerAccessor) newCb;
        IFacadeContainer oldFacades = self.getFacadeContainer();
        IFacadeContainer newFacades = newCable.getFacadeContainer();

        //processing cable visual connection
        IPart part = self.getPart(null);
        if(part != null) {
            CompoundTag visualTag = new CompoundTag();
            part.writeVisualStateToNBT(visualTag);
            if (visualTag.contains("connections")) {
                ListTag connectionsTag = visualTag.getList("connections", Tag.TAG_STRING);
                ListTag newConnectionsTag = new ListTag();
                for (Tag value : connectionsTag) {
                    StringTag connectionTag = (StringTag) value;
                    Direction connectDir = Direction.byName(connectionTag.getAsString());
                    Direction newDir = transform.rotateFacing(transform.mirrorFacing(connectDir));

                    newConnectionsTag.add(StringTag.valueOf(newDir.toString()));
                }

                visualTag.put("connections", newConnectionsTag);
                part.readVisualStateFromNBT(visualTag);
            }
            ((CableBusStorageAccessor) newCbAc.getStorage()).invokeSetCenter((ICablePart) part);
        }

        for (Direction dir : Direction.values()) {
            part = self.getPart(dir);
            Direction mapped = transform.rotateFacing(transform.mirrorFacing(dir));
            if(part != null) {
                CompoundTag spinTag = new CompoundTag();
                part.writeToNBT(spinTag);
                //Processing spin
                if(spinTag.contains("spin") && ( dir == Direction.UP || dir == Direction.DOWN )) {
                    int spin = spinTag.getByte("spin");
                    if(transform.mirror == Mirror.FRONT_BACK && (spin == 1 || spin == 3)) {
                        spin = (spin + 2) & 3;
                    } else if(transform.mirror == Mirror.LEFT_RIGHT && (spin == 0 || spin == 2)) {
                        spin = (spin + 2) & 3;
                    }
                    int steps = transform.rotation == Rotation.NONE ? 0
                            : transform.rotation == Rotation.CLOCKWISE_90 ? 1
                            : transform.rotation == Rotation.CLOCKWISE_180 ? 2
                            : 3;

                    spin = (spin + steps) & 3;
                    spinTag.putByte("spin", (byte) spin);
                    part.readFromNBT(spinTag);
                }
                ((CableBusStorageAccessor) newCbAc.getStorage()).invokeSetPart(mapped, part);
            }

            IFacadePart f = oldFacades.getFacade(dir);
            if (f != null) {
                newFacades.addFacade(new FacadePart(f.getItemStack(), mapped));
            }
        }
        newCable.saveAdditional(tag);

        self.clearContent();
        self.loadTag(tag);
    }
}