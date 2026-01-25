package com.sshakusora.create_enhanced_schematicannon.mixin.chimes.blockentity;

import com.nick.chimes.block.entity.WindBellBE;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(WindBellBE.class)
public class WindBellBEMixin implements SpecialBlockEntityItemRequirement, PartialSafeNBT {
    @Shadow @Nullable private String itemBaseColor;
    @Shadow @Nullable private String itemTagColor;

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> consumed = new ArrayList<>();

        if(this.itemBaseColor != null) {
            consumed.add(getDyeItem(this.itemBaseColor));
        }

        if(this.itemTagColor != null) {
            consumed.add(getDyeItem(this.itemTagColor));
        }

        return consumed.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumed);
    }

    @Override
    public void writeSafe(CompoundTag out, HolderLookup.Provider provider) {
        WindBellBE self = (WindBellBE) (Object) this;
        CompoundTag tag = self.getUpdateTag(provider);

        out.merge(tag);
    }

    @Unique
    @Nullable
    private DyeColor dyeColorFromString(@Nullable String string) {
        return DyeColor.byName(string, null);
    }

    @Unique
    private Item itemFromDyeColor(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }

    @Unique
    @Nullable
    public ItemStack getDyeItem(@Nullable String string) {
        DyeColor color = dyeColorFromString(string);
        if (color == null) {
            return null;
        }
        return new ItemStack(itemFromDyeColor(color));
    }
}
