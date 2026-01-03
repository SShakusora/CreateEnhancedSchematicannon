package com.sshakusora.create_enhanced_schematicannon.mixin.create.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlazeBurnerBlock.class)
public class BlazeBurnerBlockMixin implements ISpecialBlockItemRequirement {
    @Override
    public ItemRequirement getRequiredItems(BlockState var1, BlockEntity var2) {
        List<ItemStack> consumed = new ArrayList<>();

        if(var1.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.NONE) {
            consumed.add(AllItems.EMPTY_BLAZE_BURNER.asStack());
        } else consumed.add(AllBlocks.BLAZE_BURNER.asStack());

        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }
}
