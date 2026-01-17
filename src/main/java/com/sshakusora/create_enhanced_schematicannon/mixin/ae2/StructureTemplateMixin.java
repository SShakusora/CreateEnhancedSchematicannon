package com.sshakusora.create_enhanced_schematicannon.mixin.ae2;

import com.sshakusora.create_enhanced_schematicannon.network.INeedSyncBlock;
import com.sshakusora.create_enhanced_schematicannon.network.packet.client.ClientBlockEntityDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    private boolean isGetCache = false;
    @Redirect(method = "fillFromWorld", at = @At(value = "NEW", target = "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;", ordinal = 0))
    private StructureTemplate.StructureBlockInfo fillFromWorld(BlockPos pos, BlockState state, CompoundTag tag) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return new StructureTemplate.StructureBlockInfo(pos, state, tag);

        if (!(state.getBlock() instanceof INeedSyncBlock)) return new StructureTemplate.StructureBlockInfo(pos, state, tag);

        System.out.println("call3" + state.getBlock());
        CompoundTag serverTag = ClientBlockEntityDataCache.get(pos);
        if (serverTag == null) return new StructureTemplate.StructureBlockInfo(pos, state, tag);

        System.out.println("call4" + serverTag);
        this.isGetCache = true;
        return new StructureTemplate.StructureBlockInfo(pos, state, serverTag);
    }

    @Inject(method = "fillFromWorld", at = @At("TAIL"))
    private void fillFromWorld(Level p_163803_, BlockPos p_163804_, Vec3i p_163805_, boolean p_163806_, Block p_163807_, CallbackInfo ci) {
        if (this.isGetCache) ClientBlockEntityDataCache.clear();
    }
}
