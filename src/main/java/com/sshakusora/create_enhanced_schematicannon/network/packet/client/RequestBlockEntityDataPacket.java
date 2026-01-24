package com.sshakusora.create_enhanced_schematicannon.network.packet.client;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.network.packet.SyncBlockEntityDataPacket;
import com.sshakusora.create_enhanced_schematicannon.sync.server.ServerSchematicHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record RequestBlockEntityDataPacket(String fileName, boolean convertImmediately, BlockPos first, BlockPos second) implements CustomPacketPayload {
    public static final Type<RequestBlockEntityDataPacket> TYPE = new Type<>(CES.rl("request_block_entity_data"));

    public static final StreamCodec<FriendlyByteBuf, RequestBlockEntityDataPacket> STREAM_CODEC =
            CustomPacketPayload.codec(
                    (pkt, buf) -> {
                        buf.writeUtf(pkt.fileName());
                        buf.writeBoolean(pkt.convertImmediately());
                        buf.writeBlockPos(pkt.first());
                        buf.writeBlockPos(pkt.second());
                    },
                    buf -> new RequestBlockEntityDataPacket(
                            buf.readUtf(),
                            buf.readBoolean(),
                            buf.readBlockPos(),
                            buf.readBlockPos()
                    )
            );

    public static final IPayloadHandler<RequestBlockEntityDataPacket> HANDLE_REQUEST =
            (pkt, ctx) -> {
                ctx.enqueueWork(() -> {
                            ServerPlayer player = (ServerPlayer) ctx.player();
                            ServerLevel level = player.serverLevel();

                            if (!level.isLoaded(pkt.first()) || !level.isLoaded(pkt.second())) return;

                            CompoundTag data = ServerSchematicHandler.collectSchematicData(player, pkt.fileName, pkt.convertImmediately, level, pkt.first, pkt.second);
                            try {
                                player.connection.send(new SyncBlockEntityDataPacket(data));
                            } catch (Exception e) {
                                player.connection.send(new SyncBlockEntityDataPacket(null));
                                CES.LOGGER.error("[CES] Failed to collect schematic data for player {}", player.getGameProfile().getName());
                            }
                        }
                );
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

