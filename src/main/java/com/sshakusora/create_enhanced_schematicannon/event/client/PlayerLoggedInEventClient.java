package com.sshakusora.create_enhanced_schematicannon.event.client;

import com.sshakusora.create_enhanced_schematicannon.CES;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = CES.MODID, value = Dist.CLIENT)
public class PlayerLoggedInEventClient {
    @SubscribeEvent
    public static void onPlayerJoin(final FMLClientSetupEvent event) {
        ClientModState.serverHasMod = false;
    }
}
