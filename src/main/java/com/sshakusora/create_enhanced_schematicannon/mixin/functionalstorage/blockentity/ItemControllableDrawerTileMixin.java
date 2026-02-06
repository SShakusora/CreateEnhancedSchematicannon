package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.blockentity;

import com.buuz135.functionalstorage.block.tile.ItemControllableDrawerTile;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemControllableDrawerTile.class)
public class ItemControllableDrawerTileMixin implements PartialSafeNBT {
    @Unique ItemControllableDrawerTile self = (ItemControllableDrawerTile) (Object) this;

    @Unique
    private void clearStorage() {
        IItemHandler storage = self.getStorage();
        if (storage == null) return;
        for (int i = 0; i < storage.getSlots(); i++) {
            storage.extractItem(i, storage.getStackInSlot(i).getCount(), false);
        }
    }

    @Override
    public void writeSafe(CompoundTag out) {
        CompoundTag tag = new CompoundTag();

        self.clearControllerPos();
        if (self.getLevel() != null) {
            this.clearStorage();
            tag = self.getUpdateTag();
        }

        out.merge(tag);
    }
}
