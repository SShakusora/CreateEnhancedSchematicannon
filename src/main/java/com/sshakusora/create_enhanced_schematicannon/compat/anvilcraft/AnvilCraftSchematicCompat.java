package com.sshakusora.create_enhanced_schematicannon.compat.anvilcraft;

import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import dev.dubhe.anvilcraft.block.entity.AdvancedComparatorBlockEntity;
import dev.dubhe.anvilcraft.block.entity.BaseChuteBlockEntity;
import dev.dubhe.anvilcraft.block.entity.PulseGeneratorBlockEntity;
import dev.dubhe.anvilcraft.init.block.ModBlockEntities;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;

public final class AnvilCraftSchematicCompat {
    private AnvilCraftSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat(ModBlockEntities.ADVANCED_COMPARATOR.get(),
                AdvancedComparatorBlockEntity.class,
                null, (blockEntity, out, provider) -> out.merge(
                        SchematicCompatBootstrap.snapshot(blockEntity, provider)), null);
        SchematicCompatBootstrap.registerBlockEntityCompat(ModBlockEntities.PULSE_GENERATOR.get(),
                PulseGeneratorBlockEntity.class,
                null, (blockEntity, out, provider) -> out.merge(
                        SchematicCompatBootstrap.snapshot(blockEntity, provider)), null);
        registerChute(ModBlockEntities.CHUTE.get());
        registerChute(ModBlockEntities.MAGNETIC_CHUTE.get());
        registerChute(ModBlockEntities.SIMPLE_CHUTE.get());
    }

    private static void registerChute(net.minecraft.world.level.block.entity.BlockEntityType<?> type) {
        SchematicCompatBootstrap.registerBlockEntityCompat(type, BaseChuteBlockEntity.class,
                null, (blockEntity, out, provider) -> {
                    BaseChuteBlockEntity copy = SchematicCompatBootstrap.snapshotCopy(
                            blockEntity, provider, BaseChuteBlockEntity.class);
                    Collections.fill(copy.getFilteredItemStackHandler().getStacks(), ItemStack.EMPTY);
                    out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
                }, null);
    }
}
