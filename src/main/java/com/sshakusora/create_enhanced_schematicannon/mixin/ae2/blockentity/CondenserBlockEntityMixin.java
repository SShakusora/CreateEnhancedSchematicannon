package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.util.inv.AppEngInternalInventory;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(CondenserBlockEntity.class)
public class CondenserBlockEntityMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Final
    @Shadow private AppEngInternalInventory outputSlot;
    @Final
    @Shadow private AppEngInternalInventory storageSlot;
    @Final
    @Shadow private InternalInventory inputSlot;
    @Shadow
    private void setStoredPower(double storedPower) {}

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> consumed = new ArrayList<>();

        ItemStack is = this.storageSlot.getStackInSlot(0);
        if(!is.isEmpty()) {
            consumed.add(is);
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out) {
        CondenserBlockEntity self = (CondenserBlockEntity) (Object) this;
        CompoundTag tag = new CompoundTag();

        this.outputSlot.clear();
        this.inputSlot.clear();
        this.setStoredPower(0.0D);

        self.saveAdditional(tag);
        out.merge(tag);
    }
}
