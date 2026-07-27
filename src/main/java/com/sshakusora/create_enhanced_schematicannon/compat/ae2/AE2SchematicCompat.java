package com.sshakusora.create_enhanced_schematicannon.compat.ae2;

import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IFacadeContainer;
import appeng.api.parts.IFacadePart;
import appeng.api.parts.IPart;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.blockentity.misc.InscriberBlockEntity;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.blockentity.misc.VibrationChamberBlockEntity;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.blockentity.networking.WirelessAccessPointBlockEntity;
import appeng.blockentity.storage.DriveBlockEntity;
import appeng.blockentity.storage.IOPortBlockEntity;
import appeng.blockentity.storage.MEChestBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.facade.FacadePart;
import appeng.items.storage.BasicStorageCell;
import appeng.parts.CableBusContainer;
import appeng.parts.automation.AnnihilationPlanePart;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.misc.InterfacePart;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class AE2SchematicCompat {
    private static final Set<String> CELL_CONTENT_KEYS = Set.of("keys", "ic", "amts");

    private AE2SchematicCompat() {
    }

    public static void register() {
        SchematicRequirementRegistries.BLOCKS.register(AEBlocks.CABLE_BUS.block(),
                (state, blockEntity) -> ItemRequirement.NONE);
        SchematicRequirementRegistries.BLOCKS.register(AEBlocks.LIGHT_DETECTOR.block(),
                (state, blockEntity) -> SchematicCompatBootstrap.consume(
                        List.of(new ItemStack(AEBlocks.LIGHT_DETECTOR.asItem()))));

        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.CABLE_BUS.get(), CableBusBlockEntity.class,
                (blockEntity, state) -> cableBusRequirement(blockEntity),
                AE2SchematicCompat::writeCableBusSafe,
                AE2SchematicCompat::transformCableBus);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.CONDENSER.get(), CondenserBlockEntity.class,
                (blockEntity, state) -> SchematicCompatBootstrap.consume(
                        List.of(blockEntity.getInternalInventory().getStackInSlot(2))),
                AE2SchematicCompat::writeCondenserSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.DRIVE.get(), DriveBlockEntity.class,
                (blockEntity, state) -> driveRequirement(blockEntity),
                AE2SchematicCompat::writeStorageCellSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.INSCRIBER.get(), InscriberBlockEntity.class,
                (blockEntity, state) -> inscriberRequirement(blockEntity),
                AE2SchematicCompat::writeInscriberSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.INTERFACE.get(), InterfaceBlockEntity.class,
                (blockEntity, state) -> upgradeRequirement(blockEntity.getUpgrades()),
                AE2SchematicCompat::writeInterfaceSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.IO_PORT.get(), IOPortBlockEntity.class,
                (blockEntity, state) -> upgradeRequirement(blockEntity.getUpgrades()),
                AE2SchematicCompat::writeIoPortSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.ME_CHEST.get(), MEChestBlockEntity.class,
                (blockEntity, state) -> {
                    ItemStack cell = blockEntity.getCell();
                    return SchematicCompatBootstrap.consume(cell == null ? List.of() : List.of(cell));
                },
                AE2SchematicCompat::writeStorageCellSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.MOLECULAR_ASSEMBLER.get(),
                MolecularAssemblerBlockEntity.class,
                (blockEntity, state) -> molecularAssemblerRequirement(blockEntity),
                AE2SchematicCompat::writeMolecularAssemblerSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.PATTERN_PROVIDER.get(),
                PatternProviderBlockEntity.class,
                (blockEntity, state) -> patternProviderRequirement(blockEntity),
                AE2SchematicCompat::writePatternProviderSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.VIBRATION_CHAMBER.get(),
                VibrationChamberBlockEntity.class,
                (blockEntity, state) -> upgradeRequirement(blockEntity.getUpgrades()),
                AE2SchematicCompat::writeVibrationChamberSafe, null);
        SchematicCompatBootstrap.registerBlockEntityCompat(AEBlockEntities.WIRELESS_ACCESS_POINT.get(),
                WirelessAccessPointBlockEntity.class,
                (blockEntity, state) -> inventoryRequirement(blockEntity.getInternalInventory()),
                AE2SchematicCompat::writeWirelessAccessPointSafe, null);
    }

    private static ItemRequirement cableBusRequirement(CableBusBlockEntity blockEntity) {
        List<ItemStack> consumed = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            IPart part = blockEntity.getPart(direction);
            if (part != null) {
                part.addPartDrop(consumed, false);
                if (part instanceof UpgradeablePart upgradeablePart) {
                    addInventory(consumed, upgradeablePart.getUpgrades());
                } else if (part instanceof InterfacePart interfacePart) {
                    addInventory(consumed, interfacePart.getInterfaceLogic().getUpgrades());
                } else if (part instanceof PatternProviderPart patternProviderPart) {
                    addBlankPatterns(consumed, patternProviderPart.getLogic().getPatternInv());
                }
            }

            IFacadePart facade = blockEntity.getFacadeContainer().getFacade(direction);
            if (facade != null && facade.getTextureItem() != null && !facade.getTextureItem().isEmpty()) {
                consumed.add(facade.getTextureItem());
                consumed.add(AEParts.CABLE_ANCHOR.stack());
            }
        }

        IPart cable = blockEntity.getPart(null);
        if (cable != null) {
            cable.addPartDrop(consumed, false);
        }
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement driveRequirement(DriveBlockEntity blockEntity) {
        List<ItemStack> consumed = new ArrayList<>();
        for (int slot = 0; slot < blockEntity.getCellCount(); slot++) {
            ItemStack cell = blockEntity.getInternalInventory().getStackInSlot(slot);
            if (!cell.isEmpty() && cell.getItem() instanceof BasicStorageCell storageCell) {
                addInventory(consumed, storageCell.getUpgrades(cell));
            }
            consumed.add(cell);
        }
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement inscriberRequirement(InscriberBlockEntity blockEntity) {
        List<ItemStack> consumed = new ArrayList<>();
        addInventory(consumed, blockEntity.getUpgrades());
        InternalInventory inventory = blockEntity.getInternalInventory();
        consumed.add(inventory.getStackInSlot(0));
        consumed.add(inventory.getStackInSlot(1));
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement molecularAssemblerRequirement(MolecularAssemblerBlockEntity blockEntity) {
        List<ItemStack> consumed = new ArrayList<>();
        if (!blockEntity.acceptsPlans()) {
            consumed.add(AEItems.BLANK_PATTERN.stack());
        }
        addInventory(consumed, blockEntity.getUpgrades());
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement patternProviderRequirement(PatternProviderBlockEntity blockEntity) {
        List<ItemStack> consumed = new ArrayList<>();
        addBlankPatterns(consumed, blockEntity.getLogic().getPatternInv());
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement upgradeRequirement(IUpgradeInventory upgrades) {
        List<ItemStack> consumed = new ArrayList<>();
        addInventory(consumed, upgrades);
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static ItemRequirement inventoryRequirement(InternalInventory inventory) {
        List<ItemStack> consumed = new ArrayList<>();
        addInventory(consumed, inventory);
        return SchematicCompatBootstrap.consume(consumed);
    }

    private static void addInventory(List<ItemStack> stacks, InternalInventory inventory) {
        if (inventory == null) {
            return;
        }
        for (int slot = 0; slot < inventory.size(); slot++) {
            stacks.add(inventory.getStackInSlot(slot));
        }
    }

    private static void addBlankPatterns(List<ItemStack> stacks, InternalInventory patterns) {
        for (int slot = 0; slot < patterns.size(); slot++) {
            if (!patterns.getStackInSlot(slot).isEmpty()) {
                stacks.add(AEItems.BLANK_PATTERN.stack());
            }
        }
    }

    private static void writeCableBusSafe(CableBusBlockEntity blockEntity, CompoundTag out,
                                          HolderLookup.Provider provider) {
        CableBusBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, CableBusBlockEntity.class);
        for (Direction direction : Direction.values()) {
            IPart part = copy.getPart(direction);
            if (part instanceof InterfacePart interfacePart) {
                interfacePart.getStorage().clear();
            } else if (part instanceof PatternProviderPart patternProviderPart) {
                patternProviderPart.getLogic().getReturnInv().clear();
            } else if (part instanceof AnnihilationPlanePart annihilationPlanePart) {
                annihilationPlanePart.readFromNBT(new CompoundTag(), provider);
            }
        }
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeCondenserSafe(CondenserBlockEntity blockEntity, CompoundTag out,
                                           HolderLookup.Provider provider) {
        CondenserBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, CondenserBlockEntity.class);
        InternalInventory inventory = copy.getInternalInventory();
        inventory.setItemDirect(0, ItemStack.EMPTY);
        inventory.setItemDirect(1, ItemStack.EMPTY);
        CompoundTag tag = SchematicCompatBootstrap.snapshot(copy, provider);
        tag.putDouble("storedPower", 0.0D);
        out.merge(tag);
    }

    private static void writeInscriberSafe(InscriberBlockEntity blockEntity, CompoundTag out,
                                           HolderLookup.Provider provider) {
        InscriberBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, InscriberBlockEntity.class);
        InternalInventory inventory = copy.getInternalInventory();
        for (int slot = 2; slot < inventory.size(); slot++) {
            inventory.setItemDirect(slot, ItemStack.EMPTY);
        }
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeInterfaceSafe(InterfaceBlockEntity blockEntity, CompoundTag out,
                                           HolderLookup.Provider provider) {
        InterfaceBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, InterfaceBlockEntity.class);
        copy.getInterfaceLogic().getStorage().clear();
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeIoPortSafe(IOPortBlockEntity blockEntity, CompoundTag out,
                                        HolderLookup.Provider provider) {
        IOPortBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, IOPortBlockEntity.class);
        copy.getInternalInventory().clear();
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeMolecularAssemblerSafe(MolecularAssemblerBlockEntity blockEntity, CompoundTag out,
                                                    HolderLookup.Provider provider) {
        MolecularAssemblerBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, MolecularAssemblerBlockEntity.class);
        InternalInventory inventory = copy.getInternalInventory();
        for (int slot = 0; slot < Math.min(10, inventory.size()); slot++) {
            inventory.setItemDirect(slot, ItemStack.EMPTY);
        }
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writePatternProviderSafe(PatternProviderBlockEntity blockEntity, CompoundTag out,
                                                 HolderLookup.Provider provider) {
        PatternProviderBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, PatternProviderBlockEntity.class);
        copy.getLogic().getReturnInv().clear();
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeVibrationChamberSafe(VibrationChamberBlockEntity blockEntity, CompoundTag out,
                                                  HolderLookup.Provider provider) {
        VibrationChamberBlockEntity copy = new VibrationChamberBlockEntity(
                AEBlockEntities.VIBRATION_CHAMBER.get(), BlockPos.ZERO, blockEntity.getBlockState());
        IUpgradeInventory sourceUpgrades = blockEntity.getUpgrades();
        IUpgradeInventory targetUpgrades = copy.getUpgrades();
        for (int slot = 0; slot < sourceUpgrades.size(); slot++) {
            ItemStack upgrade = sourceUpgrades.getStackInSlot(slot);
            if (!upgrade.isEmpty()) {
                targetUpgrades.insertItem(slot, upgrade.copy(), false);
            }
        }
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeWirelessAccessPointSafe(WirelessAccessPointBlockEntity blockEntity, CompoundTag out,
                                                     HolderLookup.Provider provider) {
        WirelessAccessPointBlockEntity copy = new WirelessAccessPointBlockEntity(
                AEBlockEntities.WIRELESS_ACCESS_POINT.get(), BlockPos.ZERO, blockEntity.getBlockState());
        InternalInventory source = blockEntity.getInternalInventory();
        InternalInventory target = copy.getInternalInventory();
        for (int slot = 0; slot < source.size(); slot++) {
            ItemStack stack = source.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                target.insertItem(slot, stack.copy(), false);
            }
        }
        out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
    }

    private static void writeStorageCellSafe(net.minecraft.world.level.block.entity.BlockEntity blockEntity,
                                             CompoundTag out, HolderLookup.Provider provider) {
        CompoundTag tag = SchematicCompatBootstrap.snapshot(blockEntity, provider);
        if (tag.contains("inv", Tag.TAG_COMPOUND)) {
            removeCellContents(tag.getCompound("inv"));
        }
        out.merge(tag);
    }

    private static void removeCellContents(Tag tag) {
        if (tag instanceof CompoundTag compound) {
            for (String key : List.copyOf(compound.getAllKeys())) {
                if (CELL_CONTENT_KEYS.contains(key)) {
                    compound.remove(key);
                } else {
                    removeCellContents(compound.get(key));
                }
            }
        } else if (tag instanceof ListTag list) {
            for (Tag child : list) {
                removeCellContents(child);
            }
        }
    }

    private static void transformCableBus(CableBusBlockEntity blockEntity, StructureTransform transform) {
        if (blockEntity.getLevel() == null) {
            return;
        }
        HolderLookup.Provider provider = blockEntity.getLevel().registryAccess();
        CableBusBlockEntity transformed = new CableBusBlockEntity(
                AEBlockEntities.CABLE_BUS.get(), BlockPos.ZERO, blockEntity.getBlockState());
        CableBusContainer oldContainer = blockEntity.getCableBus();
        CableBusContainer newContainer = transformed.getCableBus();
        IFacadeContainer oldFacades = blockEntity.getFacadeContainer();
        IFacadeContainer newFacades = transformed.getFacadeContainer();

        CompoundTag oldData = new CompoundTag();
        oldContainer.writeToNBT(oldData, provider);
        CompoundTag mappedData = new CompoundTag();
        if (oldData.contains("hasRedstone")) {
            mappedData.putInt("hasRedstone", oldData.getInt("hasRedstone"));
        }

        IPart center = blockEntity.getPart(null);
        CompoundTag centerVisualTag = null;
        if (center != null) {
            if (oldData.contains("cable", Tag.TAG_COMPOUND)) {
                mappedData.put("cable", oldData.getCompound("cable").copy());
            }
            centerVisualTag = new CompoundTag();
            center.writeVisualStateToNBT(centerVisualTag);
            if (centerVisualTag.contains("connections", Tag.TAG_LIST)) {
                ListTag connections = centerVisualTag.getList("connections", Tag.TAG_STRING);
                ListTag mappedConnections = new ListTag();
                for (Tag value : connections) {
                    Direction direction = Direction.byName(value.getAsString());
                    if (direction != null) {
                        Direction mapped = transform.rotateFacing(transform.mirrorFacing(direction));
                        mappedConnections.add(StringTag.valueOf(mapped.getName()));
                    }
                }
                centerVisualTag.put("connections", mappedConnections);
            }
        }

        for (Direction direction : Direction.values()) {
            IPart part = blockEntity.getPart(direction);
            Direction mapped = transform.rotateFacing(transform.mirrorFacing(direction));
            if (part != null) {
                CompoundTag partTag = oldData.getCompound(direction.getSerializedName()).copy();
                if (partTag.contains("spin") && (direction == Direction.UP || direction == Direction.DOWN)) {
                    int spin = partTag.getByte("spin");
                    if (transform.mirror == Mirror.FRONT_BACK && (spin == 1 || spin == 3)) {
                        spin = (spin + 2) & 3;
                    } else if (transform.mirror == Mirror.LEFT_RIGHT && (spin == 0 || spin == 2)) {
                        spin = (spin + 2) & 3;
                    }
                    int steps = switch (transform.rotation) {
                        case NONE -> 0;
                        case CLOCKWISE_90 -> 1;
                        case CLOCKWISE_180 -> 2;
                        case COUNTERCLOCKWISE_90 -> 3;
                    };
                    partTag.putByte("spin", (byte) ((spin + steps) & 3));
                }
                mappedData.put(mapped.getSerializedName(), partTag);
            }

        }

        newContainer.readFromNBT(mappedData, provider);
        IPart transformedCenter = transformed.getPart(null);
        if (transformedCenter != null && centerVisualTag != null) {
            transformedCenter.readVisualStateFromNBT(centerVisualTag);
        }
        for (Direction direction : Direction.values()) {
            IFacadePart facade = oldFacades.getFacade(direction);
            if (facade != null) {
                Direction mapped = transform.rotateFacing(transform.mirrorFacing(direction));
                newFacades.addFacade(new FacadePart(facade.getBlockState(), mapped));
            }
        }

        blockEntity.clearContent();
        blockEntity.loadTag(SchematicCompatBootstrap.snapshot(transformed, provider), provider);
    }
}
