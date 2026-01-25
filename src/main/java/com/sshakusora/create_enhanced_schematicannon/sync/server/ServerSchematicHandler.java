package com.sshakusora.create_enhanced_schematicannon.sync.server;

import com.simibubi.create.content.schematics.SchematicAndQuillItem;
import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerSchematicHandler {
    public static CompoundTag collectSchematicData(ServerPlayer player, String fileName, boolean convertImmediately, ServerLevel level, BlockPos first, BlockPos second) {
        BoundingBox bb = BoundingBox.fromCorners(first, second);
        if (isOutOfBound(bb)) return null;

        BlockPos origin = new BlockPos(bb.minX(), bb.minY(), bb.minZ());
        BlockPos bounds = new BlockPos(bb.getXSpan(), bb.getYSpan(), bb.getZSpan());

        StructureTemplate structure = new StructureTemplate();
        structure.fillFromWorld(level, origin, bounds, true, Blocks.AIR);
        CompoundTag data = structure.save(new CompoundTag());
        SchematicAndQuillItem.replaceStructureVoidWithAir(data);
        SchematicAndQuillItem.clampGlueBoxes(level, new AABB(Vec3.atLowerCornerOf(origin), Vec3.atLowerCornerOf(origin.offset(bounds))), data);

        data.putString("CES_File", fileName);
        data.putBoolean("ConvertImmediately", convertImmediately);
        data.putLong("CES_Origin", origin.asLong());
        data.putLong("CES_Bounds", bounds.asLong());

        CES.LOGGER.info(
                "[CES] Player={} ({}) collect schematic | fileName={} | dim={} | size={} KB",
                player.getGameProfile().getName(),
                player.getUUID(),
                fileName,
                level.dimension().location(),
                getNBTSize(data) / 1024.0
        );

        return data;
    }

    private static int getNBTSize(CompoundTag tag) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NbtIo.writeCompressed(tag, baos);
            return baos.size();
        } catch (IOException e) {
            return -1;
        }
    }

    private static boolean isOutOfBound(BoundingBox bb) {
        long MAX_VOLUME = Config.SERVER.maxSchematicVolume.get();
        int MAX_EDGE = Config.SERVER.maxSchematicEdge.get();

        int x = bb.getXSpan();
        int y = bb.getYSpan();
        int z = bb.getZSpan();
        if (x > MAX_EDGE || y > MAX_EDGE || z > MAX_EDGE) return true;

        long volume = (long) x * y * z;
        return volume > MAX_VOLUME;
    }
}
