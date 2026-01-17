package com.sshakusora.create_enhanced_schematicannon.mixin.ae2;

import com.simibubi.create.content.schematics.SchematicExport;
import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import com.sshakusora.create_enhanced_schematicannon.network.INeedSyncBlockEntity;
import com.sshakusora.create_enhanced_schematicannon.network.packet.client.RequestBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(SchematicExport.class)
public class SchematicExportMixin {
    @Inject(method = "saveSchematic", at = @At("HEAD"), remap = false)
    private static void saveSchematic(Path dir, String fileName, boolean overwrite, Level level, BlockPos first, BlockPos second, CallbackInfoReturnable<SchematicExport.SchematicExportResult> cir) {
        if (!level.isClientSide()) return;

        int minX = Math.min(first.getX(), second.getX());
        int minY = Math.min(first.getY(), second.getY());
        int minZ = Math.min(first.getZ(), second.getZ());
        int maxX = Math.max(first.getX(), second.getX());
        int maxY = Math.max(first.getY(), second.getY());
        int maxZ = Math.max(first.getZ(), second.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockEntity be = level.getBlockEntity(pos);

                    if (be instanceof INeedSyncBlockEntity) {
                        BlockPos minPos = new BlockPos(minX, minY, minZ);
                        CESNetwork.CHANNEL.sendToServer(new RequestBlockEntityDataPacket(pos, minPos));
                    }
                }
            }
        }
    }
}
