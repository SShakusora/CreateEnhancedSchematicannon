package com.sshakusora.create_enhanced_schematicannon.network;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.network.packet.SyncBlockEntityDataPacket;
import com.sshakusora.create_enhanced_schematicannon.network.packet.client.RequestBlockEntityDataPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CESNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            CES.rl("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(
                id++,
                RequestBlockEntityDataPacket.class,
                RequestBlockEntityDataPacket::encode,
                RequestBlockEntityDataPacket::decode,
                RequestBlockEntityDataPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                SyncBlockEntityDataPacket.class,
                SyncBlockEntityDataPacket::encode,
                SyncBlockEntityDataPacket::decode,
                SyncBlockEntityDataPacket::handle
        );
    }
}
