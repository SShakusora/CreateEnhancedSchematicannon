package com.sshakusora.create_enhanced_schematicannon.util.functionalstorage;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.world.item.ItemStack;

public class UpgradeSlotSnapshot {
    private final InventoryComponent<?> upgrades;
    private final int slot;
    private final ItemStack stack;

    public UpgradeSlotSnapshot(InventoryComponent<?> upgrades, int slot, ItemStack stack) {
        this.upgrades = upgrades;
        this.slot = slot;
        this.stack = stack;
    }

    public void restore() {
        this.upgrades.setStackInSlot(this.slot, this.stack.copy());
    }
}
