package com.sshakusora.create_enhanced_schematicannon.compat.integrateddynamics;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.capability.partcontainer.PartContainerTileMultipartTicking;
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMultipartTicking;
import org.cyclops.integrateddynamics.core.part.panel.PartTypePanelVariableDriven;

import java.util.ArrayList;
import java.util.List;

public final class IntegratedDynamicsSchematicCompat {
    private IntegratedDynamicsSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat(RegistryEntries.BLOCK_ENTITY_MULTIPART_TICKING.get(),
                BlockEntityMultipartTicking.class,
                (blockEntity, state) -> {
                    List<ItemStack> parts = new ArrayList<>();
                    PartContainerTileMultipartTicking container = blockEntity.getPartContainer();
                    if (container != null) {
                        for (Direction direction : Direction.values()) {
                            IPartType part = container.getPart(direction);
                            if (part != null) {
                                parts.add(new ItemStack(part.getItem()));
                            }
                        }
                    }
                    return SchematicCompatBootstrap.consume(parts);
                },
                IntegratedDynamicsSchematicCompat::writeSafe,
                IntegratedDynamicsSchematicCompat::transform);
    }

    private static void writeSafe(BlockEntityMultipartTicking blockEntity, CompoundTag out,
                                  HolderLookup.Provider provider) {
        BlockEntityMultipartTicking copy = SchematicCompatBootstrap.snapshotCopy(
                blockEntity, provider, BlockEntityMultipartTicking.class);
        PartContainerTileMultipartTicking container = copy.getPartContainer();
        if (container != null) {
            for (Direction direction : Direction.values()) {
                if (container.hasPart(direction)) {
                    IPartState state = container.getPartState(direction);
                    if (state != null) {
                        state.generateId();
                    }
                }
            }
        }

        CompoundTag tag = SchematicCompatBootstrap.snapshot(copy, provider);
        if (tag.contains("partContainer", Tag.TAG_COMPOUND)) {
            CompoundTag containerTag = tag.getCompound("partContainer");
            if (containerTag.contains("parts", Tag.TAG_LIST)) {
                ListTag parts = containerTag.getList("parts", Tag.TAG_COMPOUND);
                for (int index = 0; index < parts.size(); index++) {
                    parts.getCompound(index).remove("inventory");
                }
            }
        }
        out.merge(tag);
    }

    private static void transform(BlockEntityMultipartTicking blockEntity, StructureTransform transform) {
        if (blockEntity.getLevel() == null) {
            return;
        }
        PartContainerTileMultipartTicking oldContainer = blockEntity.getPartContainer();
        if (oldContainer == null) {
            return;
        }

        BlockEntityMultipartTicking transformed = new BlockEntityMultipartTicking(
                BlockPos.ZERO, blockEntity.getBlockState());
        PartContainerTileMultipartTicking newContainer = transformed.getPartContainer();
        for (Direction direction : Direction.values()) {
            if (!oldContainer.hasPart(direction)) {
                continue;
            }
            IPartType part = oldContainer.getPart(direction);
            IPartState partState = oldContainer.getPartState(direction);
            if (part == null) {
                continue;
            }

            if ((direction == Direction.DOWN || direction == Direction.UP)
                    && partState instanceof PartTypePanelVariableDriven.State panelState) {
                Direction facing = panelState.getFacingRotation();
                Direction mappedFacing = transform.rotateFacing(transform.mirrorFacing(facing));
                panelState.setFacingRotation(fixedDirection(mappedFacing));
            }
            Direction mapped = transform.rotateFacing(transform.mirrorFacing(direction));
            newContainer.setPart(mapped, part, partState);
        }

        transformed.getCableFakeable().setRealCable(blockEntity.getCableFakeable().isRealCable());
        HolderLookup.Provider provider = blockEntity.getLevel().registryAccess();
        blockEntity.read(SchematicCompatBootstrap.snapshot(transformed, provider), provider);
    }

    private static Direction fixedDirection(Direction direction) {
        return switch (direction) {
            case WEST -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }
}
