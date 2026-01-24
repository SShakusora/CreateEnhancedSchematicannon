package com.sshakusora.create_enhanced_schematicannon.network.packet;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.client.ClientModState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerHandshakePacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerHandshakePacket> TYPE = new CustomPacketPayload.Type<>(CES.rl("server_handshake"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ServerHandshakePacket> STREAM_CODEC =
            CustomPacketPayload.codec(
                    (pkt, buf) -> {},
                    buf -> new ServerHandshakePacket()
            );

    public static void handle(ServerHandshakePacket payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientModState.serverHasMod = true;
        });
    }
}
