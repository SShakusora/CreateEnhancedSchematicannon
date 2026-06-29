package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.schematic;

import com.buuz135.functionalstorage.block.tile.ControllableDrawerTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.sshakusora.create_enhanced_schematicannon.util.functionalstorage.FunctionalStoragePrintContext;
import com.sshakusora.create_enhanced_schematicannon.util.functionalstorage.UpgradeSlotSnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SchematicannonBlockEntity.class)
public abstract class SchematicannonBlockEntityMixin {
    @Shadow(remap = false) public boolean skipMissing;

    @Shadow(remap = false)
    protected abstract boolean grabItemsFromAttachedInventories(ItemRequirement.StackRequirement requirement, boolean simulate);

    @Unique
    private final List<UpgradeSlotSnapshot> create_enhanced_schematicannon$functionalStorageUpgradeSnapshots = new ArrayList<>();

    @Inject(method = "tickPrinter", at = @At("HEAD"), remap = false)
    private void create_enhanced_schematicannon$enterFunctionalStoragePrintContext(CallbackInfo ci) {
        FunctionalStoragePrintContext.setSkippingMissing(this.skipMissing);
    }

    @Inject(method = "tickPrinter", at = @At("RETURN"), remap = false)
    private void create_enhanced_schematicannon$leaveFunctionalStoragePrintContext(CallbackInfo ci) {
        FunctionalStoragePrintContext.clear();
    }

    @Inject(
            method = "launchBlockOrBelt",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/BlockHelper;prepareBlockEntityData(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;)Lnet/minecraft/nbt/CompoundTag;"
            ),
            remap = false
    )
    private void create_enhanced_schematicannon$consumeAvailableFunctionalStorageUpgrades(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity, CallbackInfo ci) {
        this.create_enhanced_schematicannon$restoreFunctionalStorageUpgrades();
        if (!this.skipMissing || !(blockEntity instanceof ControllableDrawerTile<?> drawer)) return;

        this.create_enhanced_schematicannon$consumeAvailableUpgrades(drawer.getUtilityUpgrades());
        this.create_enhanced_schematicannon$consumeAvailableUpgrades(drawer.getStorageUpgrades());
    }

    @Inject(
            method = "launchBlockOrBelt",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/schematics/cannon/SchematicannonBlockEntity;launchBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void create_enhanced_schematicannon$restoreFunctionalStorageUpgradesAfterLaunch(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity, CallbackInfo ci) {
        this.create_enhanced_schematicannon$restoreFunctionalStorageUpgrades();
    }

    @Unique
    private void create_enhanced_schematicannon$consumeAvailableUpgrades(InventoryComponent<?> upgrades) {
        if (upgrades == null) return;

        for (int slot = 0; slot < upgrades.getSlots(); slot++) {
            ItemStack upgrade = upgrades.getStackInSlot(slot);
            if (upgrade.isEmpty()) continue;

            this.create_enhanced_schematicannon$functionalStorageUpgradeSnapshots.add(new UpgradeSlotSnapshot(upgrades, slot, upgrade.copy()));

            ItemRequirement.StackRequirement requirement = new ItemRequirement.StackRequirement(upgrade.copy(), ItemRequirement.ItemUseType.CONSUME);
            if (this.grabItemsFromAttachedInventories(requirement, true)) {
                this.grabItemsFromAttachedInventories(requirement, false);
                continue;
            }

            upgrades.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }

    @Unique
    private void create_enhanced_schematicannon$restoreFunctionalStorageUpgrades() {
        if (this.create_enhanced_schematicannon$functionalStorageUpgradeSnapshots.isEmpty()) return;

        for (UpgradeSlotSnapshot snapshot : this.create_enhanced_schematicannon$functionalStorageUpgradeSnapshots) {
            snapshot.restore();
        }
        this.create_enhanced_schematicannon$functionalStorageUpgradeSnapshots.clear();
    }
}
