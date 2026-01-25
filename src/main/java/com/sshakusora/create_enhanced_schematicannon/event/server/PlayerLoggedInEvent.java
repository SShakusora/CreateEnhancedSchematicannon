package com.sshakusora.create_enhanced_schematicannon.event.server;

import com.sshakusora.create_enhanced_schematicannon.CES;
import com.sshakusora.create_enhanced_schematicannon.network.packet.ServerHandshakePacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EventBusSubscriber(modid = CES.MODID)
public class PlayerLoggedInEvent {
    private static final Set<ServerPlayer> pending = new HashSet<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) { pending.add(player); }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (pending.isEmpty()) return;
        Iterator<ServerPlayer> it = pending.iterator();
        while (it.hasNext()) {
            ServerPlayer player = it.next();
            try {
                player.connection.send(new ServerHandshakePacket());
            } catch (Exception ignored) {}
            it.remove();
        }
    }
}
