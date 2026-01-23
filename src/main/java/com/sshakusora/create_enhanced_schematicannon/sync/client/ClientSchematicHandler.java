package com.sshakusora.create_enhanced_schematicannon.sync.client;

import com.simibubi.create.AllPackets;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.SchematicExport;
import com.simibubi.create.content.schematics.client.ClientSchematicLoader;
import com.simibubi.create.content.schematics.client.SchematicAndQuillHandler;
import com.simibubi.create.content.schematics.packet.InstantSchematicPacket;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.sshakusora.create_enhanced_schematicannon.CES;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ClientSchematicHandler {
    public static ClientSaveResult saveSchematic(CompoundTag data) {
        String fileName = data.getString("CES_File");
        boolean isConvertImmediately = data.getBoolean("ConvertImmediately");
        BlockPos origin = BlockPos.of(data.getLong("CES_Origin"));
        BlockPos bounds = BlockPos.of(data.getLong("CES_Bounds"));

        data.remove("CES_File");
        data.remove("ConvertImmediately");
        data.remove("CES_Origin");
        data.remove("CES_Bounds");

        Level level = Minecraft.getInstance().level;
        if (level == null) return new ClientSaveResult(null, isConvertImmediately);

        if (fileName.isEmpty()) {
            fileName = Lang.translateDirect("schematicAndQuill.fallbackName", new Object[0]).getString();
        }

        fileName = FilesHelper.findFirstValidFilename(fileName, SchematicExport.SCHEMATICS, "nbt");

        if (!fileName.endsWith(".nbt")) {
            fileName = fileName + ".nbt";
        }

        Path file = SchematicExport.SCHEMATICS.resolve(fileName).toAbsolutePath();

        try {
            Files.createDirectories(SchematicExport.SCHEMATICS);
            boolean overwritten = Files.deleteIfExists(file);

            try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE)) {
                NbtIo.writeCompressed(data, out);
            }

            SchematicExport.SchematicExportResult result =  new SchematicExport.SchematicExportResult(file, SchematicExport.SCHEMATICS, fileName, overwritten, origin, bounds);
            return new ClientSaveResult(result, isConvertImmediately);
        } catch (IOException e) {
            CES.LOGGER.error("An error occurred while saving schematic [" + fileName + "]", e);
            return new ClientSaveResult(null, isConvertImmediately);
        }
    }

    public static void handleSchematicAndQuill(SchematicExport.SchematicExportResult result, boolean convertImmediately) {
        LocalPlayer player = Minecraft.getInstance().player;
        SchematicAndQuillHandler handler = CreateClient.SCHEMATIC_AND_QUILL_HANDLER;
        if (result == null) {
            Lang.translate("schematicAndQuill.failed", new Object[0]).style(ChatFormatting.RED).sendStatus(player);
        } else {
            Path file = result.file();
            Lang.translate("schematicAndQuill.saved", new Object[]{file.getFileName()}).sendStatus(player);
            handler.firstPos = null;
            handler.secondPos = null;
            if (convertImmediately) {
                try {
                    if (!ClientSchematicLoader.validateSizeLimitation(Files.size(file))) {
                        return;
                    }

                    AllPackets.getChannel().sendToServer(new InstantSchematicPacket(result.fileName(), result.origin(), result.bounds()));
                } catch (IOException e) {
                    CES.LOGGER.error("Error instantly uploading Schematic file: " + file, e);
                }

            }
        }
    }

    public record ClientSaveResult(SchematicExport.SchematicExportResult result, boolean convertImmediately) {}
}
