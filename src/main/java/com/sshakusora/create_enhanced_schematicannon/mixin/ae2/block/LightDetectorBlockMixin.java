package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.misc.LightDetectorBlock;
import appeng.core.definitions.AEBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.sshakusora.create_enhanced_schematicannon.util.ae2.RotateMirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightDetectorBlock.class)
public abstract class LightDetectorBlockMixin extends Block implements SpecialBlockItemRequirement {
    public LightDetectorBlockMixin(Properties properties) {super(properties);}

    @Override
    public ItemRequirement getRequiredItems(BlockState var1, BlockEntity var2) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME ,AEBlocks.LIGHT_DETECTOR.asItem());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return RotateMirror.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return RotateMirror.mirror(state, mirror);
    }
}
