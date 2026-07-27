package com.sshakusora.create_enhanced_schematicannon.compat.moonlight;

import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MoonlightSchematicCompat {
    private MoonlightSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat("", ItemDisplayTile.class,
                (blockEntity, state) -> {
                    List<ItemStack> displayedItems = new ArrayList<>();
                    for (int slot = 0; slot < blockEntity.getContainerSize(); slot++) {
                        displayedItems.add(blockEntity.getItem(slot));
                    }
                    return SchematicCompatBootstrap.consume(displayedItems);
                },
                (blockEntity, out, provider) -> {
                    ItemDisplayTile copy = SchematicCompatBootstrap.snapshotCopy(
                            blockEntity, provider, ItemDisplayTile.class);
                    for (int slot = 0; slot < copy.getContainerSize(); slot++) {
                        ItemStack stack = copy.getItem(slot);
                        copy.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : new ItemStack(stack.getItem()));
                    }
                    out.merge(SchematicCompatBootstrap.snapshot(copy, provider));
                },
                null);
    }
}
