package com.sshakusora.create_enhanced_schematicannon.compat.supplementaries;

import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class SupplementariesSchematicCompat {
    private SupplementariesSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat("supplementaries", BlackboardBlockTile.class,
                null,
                (blockEntity, out, provider) -> out.merge(
                        SchematicCompatBootstrap.snapshot(blockEntity, provider)),
                null);
        SchematicCompatBootstrap.registerBlockEntityCompat("supplementaries", FrameBlockTile.class,
                (blockEntity, state) -> {
                    BlockState held = blockEntity.getHeldBlock();
                    return SchematicCompatBootstrap.consume(held == null
                            ? List.of()
                            : List.of(new ItemStack(held.getBlock())));
                },
                (blockEntity, out, provider) -> out.merge(
                        SchematicCompatBootstrap.snapshot(blockEntity, provider)),
                (blockEntity, transform) -> {
                    BlockState held = blockEntity.getHeldBlock();
                    if (held != null) {
                        blockEntity.setHeldBlock(transform.apply(held));
                        blockEntity.setChanged();
                    }
                });
    }
}
