package com.sshakusora.create_enhanced_schematicannon.mixin.vanilla.entity;

import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(ArmorStand.class)
public class ArmorStandMixin implements SpecialEntityItemRequirement, PartialSafeNBT {
    @Override
    public ItemRequirement getRequiredItems() {
        List<ItemRequirement.StackRequirement> requirements = new ArrayList<>();
        requirements.add(new ItemRequirement.StackRequirement(new ItemStack(Items.ARMOR_STAND), ItemRequirement.ItemUseType.CONSUME));

        return new ItemRequirement(requirements);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        ArmorStand self = (ArmorStand) (Object) this;
        CompoundTag tag = new CompoundTag();

        self.getAllSlots().forEach(s -> s.setCount(0));

        self.saveWithoutId(tag);
        out.merge(tag);
    }
}
