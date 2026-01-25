package com.sshakusora.create_enhanced_schematicannon;

import com.mojang.logging.LogUtils;
import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CES.MODID)
public class CES
{
    public static final String MODID = "create_enhanced_schematicannon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CES(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(CESNetwork::register);
        modContainer.registerConfig(
                ModConfig.Type.SERVER,
                Config.SERVER_SPEC
        );
    }

    public static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
