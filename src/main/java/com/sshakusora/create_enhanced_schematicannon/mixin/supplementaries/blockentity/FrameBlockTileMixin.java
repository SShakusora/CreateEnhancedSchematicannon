package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity;

import com.simibubi.create.CreateClient;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.util.MapDirection;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(FrameBlockTile.class)
public class FrameBlockTileMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Unique private final StructurePlaceSettings SETTINGS = CreateClient.SCHEMATIC_HANDLER.getTransformation().toSettings();
    @Unique private final Mirror MIRROR = SETTINGS.getMirror();
    @Unique private final Rotation ROTATION = SETTINGS.getRotation();

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
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        FrameBlockTile self = (FrameBlockTile) (Object) this;
        CompoundTag tag = new CompoundTag();

        BlockState held = self.getHeldBlock();
        try {
            if (held.hasProperty(BlockStateProperties.FACING) || held.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction dir = held.hasProperty(BlockStateProperties.FACING) ? held.getValue(BlockStateProperties.FACING) : held.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (dir != Direction.UP && dir != Direction.DOWN) {
                    Direction newDir = MapDirection.mapDir(ROTATION, MIRROR, dir);

                    if (held.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        held = held.setValue(BlockStateProperties.HORIZONTAL_FACING, newDir);
                    } else {
                        held = held.setValue(BlockStateProperties.FACING, newDir);
                    }
                }
            }
        } catch (Exception e) {
            CES.LOGGER.error("On writeSafe error:{}", String.valueOf(e));
        }

        self.setHeldBlock(held);
        tag = self.saveCustomAndMetadata(provider);
        out.merge(tag);
    }
}
