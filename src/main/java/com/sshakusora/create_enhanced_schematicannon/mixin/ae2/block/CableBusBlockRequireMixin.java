package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.networking.CableBusBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CableBusBlock.class)
public abstract class CableBusBlockRequireMixin extends Block implements ISpecialBlockItemRequirement {
    public CableBusBlockRequireMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState var1, BlockEntity var2) {
        return ItemRequirement.NONE;
    }
}
