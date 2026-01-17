package com.sshakusora.create_enhanced_schematicannon.network.packet.client;

import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import com.sshakusora.create_enhanced_schematicannon.network.INeedSyncBlockEntity;
import com.sshakusora.create_enhanced_schematicannon.network.packet.SyncBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class RequestBlockEntityDataPacket {
    private final BlockPos pos;
    private final BlockPos minPos;

    public RequestBlockEntityDataPacket(BlockPos pos, BlockPos minPos) {
        this.pos = pos;
        this.minPos = minPos;
    }

    public RequestBlockEntityDataPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.minPos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBlockPos(minPos);
    }

    public static void handle(RequestBlockEntityDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (!level.isLoaded(msg.pos)) return;

            BlockEntity be = level.getBlockEntity(msg.pos);
            if (be instanceof INeedSyncBlockEntity) {
                CompoundTag tag = be.saveWithId();

                BlockPos min = msg.minPos != null ? msg.minPos : BlockPos.ZERO;
                BlockPos relative = msg.pos.subtract(min);

                CESNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SyncBlockEntityDataPacket(relative, tag)
                );
            }
        });

        ctx.get().setPacketHandled(true);
    }
}

