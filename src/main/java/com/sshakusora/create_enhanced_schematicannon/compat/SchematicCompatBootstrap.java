package com.sshakusora.create_enhanced_schematicannon.compat;

import com.simibubi.create.api.contraption.transformable.MovedBlockTransformerRegistries;
import com.simibubi.create.api.schematic.nbt.SafeNbtWriterRegistry;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.sshakusora.create_enhanced_schematicannon.CES;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class SchematicCompatBootstrap {
    private static boolean registered;

    private SchematicCompatBootstrap() {
    }

    public static synchronized void register() {
        if (registered) {
            return;
        }
        registered = true;

        registerIfLoaded("ae2", "com.sshakusora.create_enhanced_schematicannon.compat.ae2.AE2SchematicCompat");
        registerIfLoaded("amendments", "com.sshakusora.create_enhanced_schematicannon.compat.amendments.AmendmentsSchematicCompat");
        registerIfLoaded("anvilcraft", "com.sshakusora.create_enhanced_schematicannon.compat.anvilcraft.AnvilCraftSchematicCompat");
        registerIfLoaded("chimes", "com.sshakusora.create_enhanced_schematicannon.compat.chimes.ChimesSchematicCompat");
        registerIfLoaded("dramaticdoors", "com.sshakusora.create_enhanced_schematicannon.compat.dramaticdoors.DramaticDoorsSchematicCompat");
        registerIfLoaded("functionalstorage", "com.sshakusora.create_enhanced_schematicannon.compat.functionalstorage.FunctionalStorageSchematicCompat");
        registerIfLoaded("integrateddynamics", "com.sshakusora.create_enhanced_schematicannon.compat.integrateddynamics.IntegratedDynamicsSchematicCompat");
        registerIfLoaded("moonlight", "com.sshakusora.create_enhanced_schematicannon.compat.moonlight.MoonlightSchematicCompat");
        registerIfLoaded("supplementaries", "com.sshakusora.create_enhanced_schematicannon.compat.supplementaries.SupplementariesSchematicCompat");
    }

    private static void registerIfLoaded(String modId, String className) {
        if (!ModList.get().isLoaded(modId)) {
            return;
        }
        try {
            Class.forName(className).getMethod("register").invoke(null);
            CES.LOGGER.debug("Registered schematic compatibility for {}", modId);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not register schematic compatibility for " + modId, exception);
        }
    }

    public static <T extends BlockEntity> List<BlockEntityType<?>> findBlockEntityTypes(
            String namespace, Class<T> targetClass) {
        List<BlockEntityType<?>> matches = new ArrayList<>();
        for (BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
            ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type);
            if (id == null || (!namespace.isEmpty() && !namespace.equals(id.getNamespace()))
                    || type.getValidBlocks().isEmpty()) {
                continue;
            }

            for (Block block : type.getValidBlocks()) {
                try {
                    BlockEntity instance = type.create(BlockPos.ZERO, block.defaultBlockState());
                    if (targetClass.isInstance(instance)) {
                        matches.add(type);
                        break;
                    }
                } catch (RuntimeException | LinkageError exception) {
                    CES.LOGGER.trace("Skipping block {} while discovering block entity type {} as {}: {}",
                            BuiltInRegistries.BLOCK.getKey(block), id, targetClass.getName(), exception.toString());
                }
            }
        }
        if (matches.isEmpty()) {
            CES.LOGGER.warn("No block entity types found for {}", targetClass.getName());
        }
        return matches;
    }

    public static <T extends Block> List<T> findBlocks(String namespace, Class<T> targetClass) {
        List<T> matches = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if ((namespace.isEmpty() || namespace.equals(id.getNamespace())) && targetClass.isInstance(block)) {
                matches.add(targetClass.cast(block));
            }
        }
        return matches;
    }

    public static <T extends BlockEntity> void registerBlockEntityCompat(
            String namespace,
            Class<T> targetClass,
            @Nullable BiFunction<T, BlockState, ItemRequirement> requirement,
            @Nullable SafeWriter<T> safeWriter,
            @Nullable BiConsumer<T, StructureTransform> transformer) {
        for (BlockEntityType<?> type : findBlockEntityTypes(namespace, targetClass)) {
            registerBlockEntityCompat(type, targetClass, requirement, safeWriter, transformer);
        }
    }

    public static <T extends BlockEntity> void registerBlockEntityCompat(
            BlockEntityType<?> type,
            Class<T> targetClass,
            @Nullable BiFunction<T, BlockState, ItemRequirement> requirement,
            @Nullable SafeWriter<T> safeWriter,
            @Nullable BiConsumer<T, StructureTransform> transformer) {
        if (requirement != null) {
            SchematicRequirementRegistries.BLOCK_ENTITIES.register(type,
                    (blockEntity, state) -> requirement.apply(targetClass.cast(blockEntity), state));
        }
        if (safeWriter != null) {
            SafeNbtWriterRegistry.REGISTRY.register(type,
                    (blockEntity, out, provider) -> safeWriter.write(targetClass.cast(blockEntity), out, provider));
        }
        if (transformer != null) {
            MovedBlockTransformerRegistries.BLOCK_ENTITY_TRANSFORMERS.register(type,
                    (blockEntity, transform) -> transformer.accept(targetClass.cast(blockEntity), transform));
        }
    }

    public static ItemRequirement consume(Iterable<ItemStack> stacks) {
        List<ItemStack> consumed = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && !stack.isEmpty()) {
                consumed.add(stack.copy());
            }
        }
        return consumed.isEmpty()
                ? ItemRequirement.NONE
                : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    public static CompoundTag snapshot(BlockEntity blockEntity, HolderLookup.Provider provider) {
        return blockEntity.saveCustomOnly(provider);
    }

    public static <T extends BlockEntity> T snapshotCopy(T source, HolderLookup.Provider provider, Class<T> targetClass) {
        BlockEntity copy = source.getType().create(source.getBlockPos(), source.getBlockState());
        if (!targetClass.isInstance(copy)) {
            throw new IllegalStateException("Could not copy block entity " + source.getType());
        }
        if (source.getLevel() != null) {
            copy.setLevel(source.getLevel());
        }
        copy.loadCustomOnly(snapshot(source, provider), provider);
        return targetClass.cast(copy);
    }

    @FunctionalInterface
    public interface SafeWriter<T extends BlockEntity> {
        void write(T blockEntity, CompoundTag out, HolderLookup.Provider provider);
    }
}
