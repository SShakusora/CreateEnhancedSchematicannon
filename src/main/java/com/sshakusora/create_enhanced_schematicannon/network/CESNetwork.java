package com.sshakusora.create_enhanced_schematicannon.network;

import com.sshakusora.create_enhanced_schematicannon.network.packet.SyncBlockEntityDataPacket;
import com.sshakusora.create_enhanced_schematicannon.network.packet.client.RequestBlockEntityDataPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class CESNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {

        event.registrar("1")
                .optional()
                .playToServer(
                        RequestBlockEntityDataPacket.TYPE,
                        RequestBlockEntityDataPacket.STREAM_CODEC,
                        RequestBlockEntityDataPacket.HANDLE_REQUEST
                )
                .playToClient(
                        SyncBlockEntityDataPacket.TYPE,
                        SyncBlockEntityDataPacket.STREAM_CODEC,
                        SyncBlockEntityDataPacket.HANDLE_REQUEST
                );
    }
}
