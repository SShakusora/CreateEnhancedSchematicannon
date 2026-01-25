package com.sshakusora.create_enhanced_schematicannon.event.client;

import com.sshakusora.create_enhanced_schematicannon.CES;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = CES.MODID, value = Dist.CLIENT)
public class PlayerLoggedInEventClient {
    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientModState.serverHasMod = false;
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientModState.serverHasMod = false;
    }
}
