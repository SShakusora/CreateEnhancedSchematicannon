package com.sshakusora.create_enhanced_schematicannon.mixin.vanilla;

import com.sshakusora.create_enhanced_schematicannon.CES;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ForcedChunksSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;

@Mixin(ForcedChunksSavedData.class)
public class ForgeChunkSaveMixin {
    @Shadow
    private LongSet chunks;
    @Inject(
            method = "Lnet/minecraft/world/level/ForcedChunksSavedData;save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            at = @At(value = "HEAD"),
            remap = true,
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void noSaveForceLoad(CompoundTag p_46120_, CallbackInfoReturnable<CompoundTag> cir){
        try{
            p_46120_.putLongArray("Forced", this.chunks.toLongArray());
        }catch (Exception e){
            p_46120_.putLongArray("Forced", new ArrayList<>());
            CES.LOGGER.debug("Forced chunks is empty");
        }
        cir.setReturnValue(p_46120_);
        return;
    }
}
