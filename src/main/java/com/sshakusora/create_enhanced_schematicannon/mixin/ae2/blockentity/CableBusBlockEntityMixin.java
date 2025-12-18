package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IFacadeContainer;
import appeng.api.parts.IFacadePart;
import appeng.api.parts.IPart;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import appeng.core.definitions.AEItems;
import appeng.facade.FacadePart;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.parts.CableBusContainer;
import appeng.parts.automation.AnnihilationPlanePart;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.misc.InterfacePart;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusContainerAccessor;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusStorageAccessor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(CableBusBlockEntity.class)
public abstract class CableBusBlockEntityMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Unique private final StructurePlaceSettings SETTINGS = CreateClient.SCHEMATIC_HANDLER.getTransformation().toSettings();
    @Unique private final Mirror MIRROR = SETTINGS.getMirror();
    @Unique private final Rotation ROTATION = SETTINGS.getRotation();

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

            IFacadePart iFacadePart = self.getFacadeContainer().getFacade(direction);
            if (iFacadePart != null && iFacadePart.getItemStack() != null) {
                strictConsumed.add(iFacadePart.getItemStack());
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

        CableBusContainer cb = self.getCableBus();
        CableBusContainerAccessor cbac = (CableBusContainerAccessor) cb;

        CableBusBlockEntity newCable = new CableBusBlockEntity(AEBlockEntities.CABLE_BUS, self.getBlockPos(), self.getBlockState());
        CableBusContainer newCb = newCable.getCableBus();
        CableBusContainerAccessor newCbac = (CableBusContainerAccessor) newCb;

        java.util.function.Function<Direction, Direction> mapDir = d -> {
            Direction afterMirror = MIRROR != Mirror.NONE ? MIRROR.mirror(d) : d;
            return ROTATION != Rotation.NONE ? ROTATION.rotate(afterMirror) : afterMirror;
        };

        for (Direction dir : Direction.values()) {
            IPart part = self.getPart(dir);
            if(part == null) continue;

            Direction mapped = mapDir.apply(dir);
            ((CableBusStorageAccessor) newCbac.getStorage()).invokeSetPart(mapped, part);
            self.removePartFromSide(dir);
        }

        IFacadeContainer oldFacades = self.getFacadeContainer();
        IFacadeContainer newFacades = newCable.getFacadeContainer();

        for (Direction dir : Direction.values()) {
            IFacadePart f = oldFacades.getFacade(dir);
            if (f == null) continue;

            Direction mapped = mapDir.apply(dir);
            newFacades.addFacade(new FacadePart(f.getItemStack(), mapped));
            oldFacades.removeFacade(self.getCableBus(), dir);
        }

        for (Direction dir : Direction.values()) {
            IPart newPart = newCable.getPart(dir);
            if (newPart == null) continue;

            ((CableBusStorageAccessor) cbac.getStorage()).invokeSetPart(dir, newPart);
        }

        IFacadeContainer origFacades = self.getFacadeContainer();
        IFacadeContainer srcFacades = newCable.getFacadeContainer();

        for (Direction dir : Direction.values()) {
            IFacadePart oldF = origFacades.getFacade(dir);
            if (oldF != null) {
                origFacades.removeFacade(self.getCableBus(), dir);
            }

            IFacadePart newF = srcFacades.getFacade(dir);
            if (newF != null) {
                origFacades.addFacade(newF);
            }
        }

        self.getCableBus().updateConnections();
        self.saveAdditional(tag);
        out.merge(tag);
    }
}