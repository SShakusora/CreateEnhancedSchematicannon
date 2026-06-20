package com.sshakusora.create_enhanced_schematicannon.network.packet;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.sync.client.ClientSchematicHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.Nullable;

public record SyncBlockEntityDataPacket(@Nullable CompoundTag data) implements CustomPacketPayload {
    public static final Type<SyncBlockEntityDataPacket> TYPE = new Type<>(CES.rl("sync_block_entity_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncBlockEntityDataPacket> STREAM_CODEC =
            CustomPacketPayload.codec(
                    (pkt, buf) -> buf.writeNbt(pkt.data()),
                    buf -> new SyncBlockEntityDataPacket((CompoundTag) buf.readNbt(NbtAccounter.unlimitedHeap()))
            );

    public static final IPayloadHandler<SyncBlockEntityDataPacket> HANDLE_REQUEST =
            (pkt, ctx) -> {
                ctx.enqueueWork(() -> {
                    ClientSchematicHandler.ClientSaveResult result = ClientSchematicHandler.saveSchematic(pkt.data());
                    ClientSchematicHandler.handleSchematicAndQuill(result.result(), result.convertImmediately());
                });
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {return TYPE;}
}