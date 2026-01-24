package com.sshakusora.create_enhanced_schematicannon.mixin.create.debug;

import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.sshakusora.create_enhanced_schematicannon.CES;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SchematicannonBlockEntity.class)
public class FindBlockEntity {
    @Inject(method = "launchBlockOrBelt", at = @At("HEAD"), remap = false)
    private void findBlockEntity(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity != null) {
            CES.LOGGER.debug("[CES] Found block entity class: {}", blockEntity.getClass());
        }
    }
}
