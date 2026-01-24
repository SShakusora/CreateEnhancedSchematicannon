package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity;

import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlackboardBlockTile.class)
public class BlackboardBlockTileMixin implements PartialSafeNBT {
    @Unique private BlackboardBlockTile self = (BlackboardBlockTile) (Object) this;

    @Override
    public void writeSafe(CompoundTag out) {
        CompoundTag tag = new CompoundTag();

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
