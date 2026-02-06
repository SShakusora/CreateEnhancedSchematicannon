package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.blockentity;

import com.buuz135.functionalstorage.block.tile.FluidDrawerTile;
import com.buuz135.functionalstorage.fluid.BigFluidHandler;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FluidDrawerTile.class)
public class FluidDrawerTileMixin implements PartialSafeNBT {
    @Unique FluidDrawerTile self = (FluidDrawerTile) (Object) this;

    @Unique
    private void clearStorage() {
        BigFluidHandler storage = self.getFluidHandler();
        for (BigFluidHandler.CustomFluidTank tank : storage.getTankList()) {
            tank.setFluid(FluidStack.EMPTY);
        }
        storage.onChange();
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
