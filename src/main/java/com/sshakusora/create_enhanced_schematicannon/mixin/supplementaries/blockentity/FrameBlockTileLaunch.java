package com.sshakusora.create_enhanced_schematicannon.mixin.supplementaries.blockentity;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.util.MapDirection;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SchematicannonBlockEntity.class)
public class FrameBlockTileLaunch {
    @Inject(method = "launchBlockOrBelt", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/cannon/SchematicannonBlockEntity;launchBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void inject$modifyNBT(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity, CallbackInfo ci, CompoundTag data) {
        if (!(blockEntity instanceof FrameBlockTile self)) return;

        SchematicannonBlockEntity SBEntity = (SchematicannonBlockEntity) (Object) this;
        ItemStack blueprint = SBEntity.inventory.getStackInSlot(0);

        Rotation rotation = blueprint.getOrDefault(
                AllDataComponents.SCHEMATIC_ROTATION,
                Rotation.NONE
        );
        Mirror mirror = blueprint.getOrDefault(
                AllDataComponents.SCHEMATIC_MIRROR,
                Mirror.NONE
        );

        CompoundTag tag = new CompoundTag();
        if (self.getLevel() == null) return;
        HolderLookup.Provider provider = self.getLevel().registryAccess();

        BlockState held = self.getHeldBlock();
        try {
            if (held.hasProperty(BlockStateProperties.FACING) || held.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction dir = held.hasProperty(BlockStateProperties.FACING) ? held.getValue(BlockStateProperties.FACING) : held.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (dir != Direction.UP && dir != Direction.DOWN) {
                    Direction newDir = MapDirection.mapDir(rotation, mirror, dir);

                    if (held.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        held = held.setValue(BlockStateProperties.HORIZONTAL_FACING, newDir);
                    } else {
                        held = held.setValue(BlockStateProperties.FACING, newDir);
                    }
                }
            }
        } catch (Exception e) {
            CES.LOGGER.error("On writeSafe error:{}", String.valueOf(e));
        }

        self.setHeldBlock(held);
        self.saveAdditional(tag, provider);
        data.merge(tag);
    }
}
