package com.sshakusora.create_enhanced_schematicannon.network.packet;

import com.sshakusora.create_enhanced_schematicannon.network.packet.client.ClientBlockEntityDataCache;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBlockEntityDataPacket {
    private final BlockPos pos;
    private final CompoundTag tag;

    public SyncBlockEntityDataPacket(BlockPos pos, CompoundTag tag) {
        this.pos = pos;
        this.tag = tag;
    }

    public SyncBlockEntityDataPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.tag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeNbt(tag);
    }

    public static void handle(SyncBlockEntityDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientBlockEntityDataCache.put(msg.pos, msg.tag);
        });

        ctx.get().setPacketHandled(true);
    }
}

