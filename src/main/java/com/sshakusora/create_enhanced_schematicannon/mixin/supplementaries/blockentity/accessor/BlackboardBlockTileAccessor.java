package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity.accessor;

import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlackboardBlockTile.class)
public interface BlackboardBlockTileAccessor {
    @Invoker("saveAdditional")
    void invokeSaveAdditional(CompoundTag tag, HolderLookup.Provider registries);
}
