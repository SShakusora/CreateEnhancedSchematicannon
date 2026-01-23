package com.sshakusora.create_enhanced_schematicannon.network.packet;

import com.sshakusora.create_enhanced_schematicannon.sync.client.ClientSchematicHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncBlockEntityDataPacket(CompoundTag data) {
    public static void encode(SyncBlockEntityDataPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SyncBlockEntityDataPacket decode(FriendlyByteBuf buf) {
        return new SyncBlockEntityDataPacket(buf.readNbt());
    }

    public static void handle(SyncBlockEntityDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientSchematicHandler.ClientSaveResult result = ClientSchematicHandler.saveSchematic(msg.data());
            ClientSchematicHandler.handleSchematicAndQuill(result.result(), result.convertImmediately());
        });
        ctx.get().setPacketHandled(true);
    }
}

