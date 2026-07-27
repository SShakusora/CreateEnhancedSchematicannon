package com.sshakusora.create_enhanced_schematicannon.compat.amendments;

import com.simibubi.create.api.schematic.nbt.SafeNbtWriterRegistry;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.mehvahdjukaar.amendments.common.ExtendedHangingSign;
import net.mehvahdjukaar.amendments.common.tile.HangingSignTileExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;

import java.util.List;

public final class AmendmentsSchematicCompat {
    private AmendmentsSchematicCompat() {
    }

    public static void register() {
        SchematicRequirementRegistries.BLOCK_ENTITIES.register(BlockEntityType.HANGING_SIGN,
                (blockEntity, state) -> {
                    HangingSignTileExtension extension = ((ExtendedHangingSign) blockEntity).amendments$getExtension();
                    return SchematicCompatBootstrap.consume(List.of(
                            extension.getFrontItem(), extension.getBackItem()));
                });
        SafeNbtWriterRegistry.REGISTRY.register(BlockEntityType.HANGING_SIGN,
                (blockEntity, out, provider) -> {
                    HangingSignBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                            (HangingSignBlockEntity) blockEntity, provider, HangingSignBlockEntity.class);
                    HangingSignTileExtension extension = ((ExtendedHangingSign) copy).amendments$getExtension();
                    extension.setFrontItem(withoutData(extension.getFrontItem()));
                    extension.setBackItem(withoutData(extension.getBackItem()));
                    out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
                });
    }

    private static ItemStack withoutData(ItemStack stack) {
        return stack.isEmpty() ? ItemStack.EMPTY : new ItemStack(stack.getItem());
    }
}
