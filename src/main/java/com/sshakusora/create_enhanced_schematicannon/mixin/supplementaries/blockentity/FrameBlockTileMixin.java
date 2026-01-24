package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity;

import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(FrameBlockTile.class)
public class FrameBlockTileMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT, TransformableBlockEntity {
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
    public void writeSafe(CompoundTag out) {
        FrameBlockTile self = (FrameBlockTile) (Object) this;
        CompoundTag tag = new CompoundTag();

        self.saveAdditional(tag);
        out.merge(tag);
    }

    @Override
    public void transform(BlockEntity be, StructureTransform transform) {
        FrameBlockTile self = (FrameBlockTile) (Object) this;
        BlockState held = self.getHeldBlock();
        if(held == null) return;

        self.setHeldBlock(transform.apply(held));
        self.setChanged();
    }
}
