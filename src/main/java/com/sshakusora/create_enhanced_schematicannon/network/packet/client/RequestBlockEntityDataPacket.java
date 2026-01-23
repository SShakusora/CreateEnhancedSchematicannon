package com.sshakusora.create_enhanced_schematicannon.network.packet.client;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import com.sshakusora.create_enhanced_schematicannon.network.packet.SyncBlockEntityDataPacket;
import com.sshakusora.create_enhanced_schematicannon.sync.server.ServerSchematicHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record RequestBlockEntityDataPacket(String fileName, boolean convertImmediately,BlockPos first, BlockPos second) {
    public static void encode(RequestBlockEntityDataPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.fileName);
        buf.writeBoolean(msg.convertImmediately);
        buf.writeBlockPos(msg.first);
        buf.writeBlockPos(msg.second);
    }

    public static RequestBlockEntityDataPacket decode(FriendlyByteBuf buffer) {
        return new RequestBlockEntityDataPacket(buffer.readUtf(), buffer.readBoolean(), buffer.readBlockPos(), buffer.readBlockPos());
    }

    public static void handle(RequestBlockEntityDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            if (!level.isLoaded(msg.first) || !level.isLoaded(msg.second)) return;

            CompoundTag data = ServerSchematicHandler.collectSchematicData(player, msg.fileName, msg.convertImmediately, level, msg.first, msg.second);

            try {
                CESNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SyncBlockEntityDataPacket(data)
                );
            } catch (Exception e) {
                CESNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SyncBlockEntityDataPacket(null)
                );
                CES.LOGGER.error("[CES] Failed to send schematic data packet to player: {}", player.getGameProfile().getName());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

