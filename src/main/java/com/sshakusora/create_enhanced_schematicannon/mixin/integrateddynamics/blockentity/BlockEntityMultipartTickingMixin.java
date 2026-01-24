package com.sshakusora.create_enhanced_schematicannon.mixin.integrateddynamics.blockentity;

import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.capability.partcontainer.PartContainerTileMultipartTicking;
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMultipartTicking;
import org.cyclops.integrateddynamics.core.part.panel.PartTypePanelVariableDriven;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockEntityMultipartTicking.class)
public abstract class BlockEntityMultipartTickingMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT, TransformableBlockEntity {
    @Unique BlockEntityMultipartTicking self = (BlockEntityMultipartTicking) (Object) this;

    @Unique
    private Direction fixedDirection(Direction dir) {
        return switch (dir) {
            case WEST -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> consumed = new ArrayList<>();

        PartContainerTileMultipartTicking partContainer = self.getPartContainer();
        for (Direction dir : Direction.values()) {
            IPartType part = partContainer.getPart(dir);
            if (part == null) continue;

            ItemStack itemStack = new ItemStack(part.getItem());
            consumed.add(itemStack);
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        PartContainerTileMultipartTicking partContainer = self.getPartContainer();
        if (partContainer == null) return;
        for (Direction dir : Direction.values()) {
            if (!partContainer.hasPart(dir)) continue;
            IPartState partState = partContainer.getPartState(dir);
            if (partState == null) continue;

            partState.generateId();
        }

        CompoundTag tag = new CompoundTag();
        self.saveAdditional(tag);

        if (tag.contains("partContainer")) {
            CompoundTag partContainerTag = tag.getCompound("partContainer");
            if (partContainerTag.contains("parts")) {
                ListTag parts = partContainerTag.getList("parts", Tag.TAG_COMPOUND);
                for (int i = 0; i < parts.size(); i++) {
                    if (parts.getCompound(i).contains("inventory")) {
                        parts.getCompound(i).remove("inventory");
                    }
                }
            }
        }

        out.merge(tag);
    }

    @Override
    public void transform(BlockEntity be, StructureTransform transform) {
        BlockEntityMultipartTicking newCable = new BlockEntityMultipartTicking(BlockPos.ZERO, self.getBlockState());
        PartContainerTileMultipartTicking newPartContainer = newCable.getPartContainer();
        PartContainerTileMultipartTicking partContainer = self.getPartContainer();

        if (partContainer == null) return;
        for (Direction dir : Direction.values()) {
            if (!partContainer.hasPart(dir)) continue;
            IPartType part = partContainer.getPart(dir);
            IPartState partState = partContainer.getPartState(dir);
            if (part == null) continue;

            if ((dir == Direction.DOWN || dir == Direction.UP) && partState instanceof PartTypePanelVariableDriven.State spPartState) {
                Direction facing = spPartState.getFacingRotation();
                Direction mapped = transform.rotateFacing(transform.mirrorFacing(facing));

                spPartState.setFacingRotation(this.fixedDirection(mapped));
            }
            Direction mapped = transform.rotateFacing(transform.mirrorFacing(dir));
            newPartContainer.setPart(mapped, part, partState);
        }

        newCable.getCableFakeable().setRealCable(self.getCableFakeable().isRealCable());

        CompoundTag tag = new CompoundTag();
        newCable.saveAdditional(tag);
        self.read(tag);
    }
}
