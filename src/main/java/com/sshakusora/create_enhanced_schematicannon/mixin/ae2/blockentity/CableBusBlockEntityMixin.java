package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.parts.IPart;
import appeng.blockentity.networking.CableBusBlockEntity;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(CableBusBlockEntity.class)
public class CableBusBlockEntityMixin implements ISpecialBlockEntityItemRequirement{
    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        CableBusBlockEntity self = (CableBusBlockEntity) (Object) this;

        List<ItemStack> consumed = new ArrayList<>();

        for(Direction direction : Direction.values()) {
            IPart part = self.getPart(direction);
            if(part != null) {
                part.addPartDrop(consumed, false);
                part.addAdditionalDrops(consumed, false);
            }
        }

        IPart cable = self.getPart(null);
        if (cable != null) {
            cable.addPartDrop(consumed, false);
            cable.addAdditionalDrops(consumed, false);
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }
}
