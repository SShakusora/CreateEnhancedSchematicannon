package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.blockentity;

import com.buuz135.functionalstorage.block.tile.DrawerControllerTile;
import com.buuz135.functionalstorage.util.ConnectedDrawers;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrawerControllerTile.class)
public class DrawerControllerTileMixin implements PartialSafeNBT {
    @Override
    public void writeSafe(CompoundTag out) {
        DrawerControllerTile self = (DrawerControllerTile) (Object) this;
        CompoundTag tag = new CompoundTag();

        ConnectedDrawers connectedDrawers = self.getConnectedDrawers();
        connectedDrawers.getConnectedDrawers().clear();

        if (self.getLevel() != null) {
            tag = self.getUpdateTag();
        }

        out.merge(tag);
    }
}
