package com.sshakusora.create_enhanced_schematicannon.compat.chimes;

import com.nick.chimes.block.ChimesBlocks;
import com.nick.chimes.block.entity.WindBellBE;
import com.nick.chimes.component.ChimesComponents;
import com.sshakusora.create_enhanced_schematicannon.compat.SchematicCompatBootstrap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class ChimesSchematicCompat {
    private ChimesSchematicCompat() {
    }

    public static void register() {
        SchematicCompatBootstrap.registerBlockEntityCompat(ChimesBlocks.WIND_BELL_BLOCK_ENTITY, WindBellBE.class,
                (blockEntity, state) -> {
                    ItemStack bell = blockEntity.getItem();
                    List<ItemStack> dyes = new ArrayList<>();
                    addDye(dyes, bell.get(ChimesComponents.GLASS_BELL_BASE_COLOR));
                    addDye(dyes, bell.get(ChimesComponents.GLASS_BELL_TAG_COLOR));
                    return SchematicCompatBootstrap.consume(dyes);
                },
                (blockEntity, out, provider) -> out.merge(
                        SchematicCompatBootstrap.snapshot(blockEntity, provider)),
                null);
    }

    private static void addDye(List<ItemStack> stacks, String colorName) {
        DyeColor color = DyeColor.byName(colorName, null);
        if (color != null) {
            stacks.add(new ItemStack(dyeItem(color)));
        }
    }

    private static Item dyeItem(DyeColor color) {
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
}
