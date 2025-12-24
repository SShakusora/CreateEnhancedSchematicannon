package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity;

import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(FrameBlockTile.class)
public class FrameBlockTileMixin implements ISpecialBlockEntityItemRequirement, IPartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        FrameBlockTile self = (FrameBlockTile) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        BlockState held = self.getHeldBlock();
        if(held != null) {
            consumed.add(new ItemStack(held.getBlock()));
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {}
}
