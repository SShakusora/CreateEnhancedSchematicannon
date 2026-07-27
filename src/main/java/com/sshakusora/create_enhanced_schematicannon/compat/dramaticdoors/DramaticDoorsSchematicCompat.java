package com.sshakusora.create_enhanced_schematicannon.compat.dramaticdoors;

import com.fizzware.dramaticdoors.neoforge.blocks.TallDoorBlock;
import com.fizzware.dramaticdoors.neoforge.state.properties.TripleBlockPart;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class DramaticDoorsSchematicCompat {
    private DramaticDoorsSchematicCompat() {
    }

    public static void register() {
        for (TallDoorBlock block : SchematicCompatBootstrap.findBlocks("", TallDoorBlock.class)) {
            SchematicRequirementRegistries.BLOCKS.register(block, (state, blockEntity) ->
                    state.getValue(TallDoorBlock.THIRD) == TripleBlockPart.LOWER
                            ? SchematicCompatBootstrap.consume(List.of(new ItemStack(block)))
                            : com.simibubi.create.content.schematics.requirement.ItemRequirement.NONE);
        }
    }
}
