package com.sshakusora.create_enhanced_schematicannon.sync.client;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.SchematicExport;
import com.simibubi.create.content.schematics.client.ClientSchematicLoader;
import com.simibubi.create.content.schematics.client.SchematicAndQuillHandler;
import com.simibubi.create.content.schematics.packet.InstantSchematicPacket;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.foundation.utility.CreatePaths;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.sshakusora.create_enhanced_schematicannon.CES;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ClientSchematicHandler {
    public static ClientSaveResult saveSchematic(@Nullable CompoundTag data) {
        if (data == null) {
            return new ClientSaveResult(null, false);
        }

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
            fileName = CreateLang.translateDirect("schematicAndQuill.fallbackName", new Object[0]).getString();
        }

        fileName = FilesHelper.findFirstValidFilename(fileName, CreatePaths.SCHEMATICS_DIR, "nbt");

        if (!fileName.endsWith(".nbt")) {
            fileName = fileName + ".nbt";
        }

        Path file = CreatePaths.SCHEMATICS_DIR.resolve(fileName).toAbsolutePath();

        try {
            Files.createDirectories(CreatePaths.SCHEMATICS_DIR);
            boolean overwritten = Files.deleteIfExists(file);

            try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE)) {
                NbtIo.writeCompressed(data, out);
            }

            SchematicExport.SchematicExportResult result =  new SchematicExport.SchematicExportResult(file, CreatePaths.SCHEMATICS_DIR, fileName, overwritten, origin, bounds);
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
            if (player != null) {
                player.displayClientMessage(Component.translatable("create_enhanced_schematicannon.action.sync.fail").withStyle(ChatFormatting.RED), true);
            }
        } else {
            Path file = result.file();
            CreateLang.translate("schematicAndQuill.saved", new Object[]{file.getFileName().toString()}).sendStatus(player);
            handler.firstPos = null;
            handler.secondPos = null;
            if (convertImmediately) {
                try {
                    if (!ClientSchematicLoader.validateSizeLimitation(Files.size(file))) {
                        return;
                    }

                    CatnipServices.NETWORK.sendToServer(new InstantSchematicPacket(result.fileName(), result.origin(), result.bounds()));
                } catch (IOException e) {
                    CES.LOGGER.error("Error instantly uploading Schematic file: " + String.valueOf(file), e);
                }

            }
        }
    }

    public record ClientSaveResult(SchematicExport.SchematicExportResult result, boolean convertImmediately) {}
}
