package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.block;

import com.buuz135.functionalstorage.block.Drawer;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockHelper.class)
public class DrawerLaunch {
    @Inject(method = "placeSchematicBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;setPlacedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true, remap = false)
    private static void drawerFix(Level world, BlockState state, BlockPos target, ItemStack stack, CompoundTag data, CallbackInfo ci) {
        if (state.getBlock() instanceof Drawer) ci.cancel();
    }
}
