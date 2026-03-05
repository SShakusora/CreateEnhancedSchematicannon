package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.blockentity;

import com.buuz135.functionalstorage.block.tile.ControllableDrawerTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ControllableDrawerTile.class)
public class ControllableDrawerTileMixin implements SpecialBlockEntityItemRequirement {
    @Unique
    ControllableDrawerTile self = (ControllableDrawerTile) (Object) this;

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> consumed = new ArrayList<>();

        InventoryComponent utilityUpgrades = self.getUtilityUpgrades();
        if (utilityUpgrades != null) {
            for (int i = 0; i < utilityUpgrades.getSlots(); i++) {
                ItemStack upgrade = utilityUpgrades.getStackInSlot(i);
                consumed.add(upgrade);
            }
        }
        InventoryComponent storageUpgrades = self.getStorageUpgrades();
        if (storageUpgrades != null) {
            for (int i = 0; i < storageUpgrades.getSlots(); i++) {
                ItemStack upgrade = storageUpgrades.getStackInSlot(i);
                consumed.add(upgrade);
            }
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }
}
