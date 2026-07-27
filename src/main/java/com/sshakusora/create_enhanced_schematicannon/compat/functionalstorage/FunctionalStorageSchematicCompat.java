package com.sshakusora.create_enhanced_schematicannon.compat.functionalstorage;

import com.buuz135.functionalstorage.block.tile.ControllableDrawerTile;
import com.buuz135.functionalstorage.block.tile.EnderDrawerTile;
import com.buuz135.functionalstorage.block.tile.FluidDrawerTile;
import com.buuz135.functionalstorage.block.tile.ItemControllableDrawerTile;
import com.buuz135.functionalstorage.block.tile.StorageControllerTile;
import com.buuz135.functionalstorage.fluid.BigFluidHandler;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalStorageSchematicCompat {
    private FunctionalStorageSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat("functionalstorage", ControllableDrawerTile.class,
                (blockEntity, state) -> {
                    List<ItemStack> upgrades = new ArrayList<>();
                    addInventory(upgrades, blockEntity.getUtilityUpgrades());
                    addInventory(upgrades, blockEntity.getStorageUpgrades());
                    return SchematicCompatBootstrap.consume(upgrades);
                }, FunctionalStorageSchematicCompat::writeSafe, null);
    }

    private static void writeSafe(ControllableDrawerTile<?> blockEntity,
                                  net.minecraft.nbt.CompoundTag out,
                                  net.minecraft.core.HolderLookup.Provider provider) {
        if (blockEntity instanceof EnderDrawerTile) {
            net.minecraft.nbt.CompoundTag tag = SchematicCompatBootstrap.snapshot(blockEntity, provider);
            tag.remove("controllerPos");
            tag.remove("frequency");
            out.merge(tag);
            return;
        }

        if (blockEntity instanceof StorageControllerTile<?> controller) {
            StorageControllerTile<?> copy = SchematicCompatBootstrap.snapshotCopy(
                    controller, provider, StorageControllerTile.class);
            copy.getConnectedDrawers().getConnectedDrawers().clear();
            out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
            return;
        }

        if (blockEntity instanceof ItemControllableDrawerTile<?> itemDrawer) {
            ItemControllableDrawerTile<?> copy = SchematicCompatBootstrap.snapshotCopy(
                    itemDrawer, provider, ItemControllableDrawerTile.class);
            copy.clearControllerPos();
            IItemHandler storage = copy.getStorage();
            if (storage != null) {
                for (int slot = 0; slot < storage.getSlots(); slot++) {
                    ItemStack stack = storage.getStackInSlot(slot);
                    if (!stack.isEmpty()) {
                        storage.extractItem(slot, stack.getCount(), false);
                    }
                }
            }
            out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
        }

        if (blockEntity instanceof FluidDrawerTile fluidDrawer) {
            FluidDrawerTile copy = SchematicCompatBootstrap.snapshotCopy(
                    fluidDrawer, provider, FluidDrawerTile.class);
            copy.clearControllerPos();
            BigFluidHandler storage = copy.getFluidHandler();
            if (storage != null) {
                for (BigFluidHandler.CustomFluidTank tank : storage.getTankList()) {
                    tank.setFluid(FluidStack.EMPTY);
                }
                storage.onChange();
            }
            out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
            return;
        }

        // Unknown direct subclasses did not inherit a safe-NBT implementation before this registry migration.
        // Keep their custom data excluded instead of accidentally marking all of it as schematic-safe.
    }

    private static void addInventory(List<ItemStack> stacks, InventoryComponent<?> inventory) {
        if (inventory == null) {
            return;
        }
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            stacks.add(inventory.getStackInSlot(slot));
        }
    }
}
