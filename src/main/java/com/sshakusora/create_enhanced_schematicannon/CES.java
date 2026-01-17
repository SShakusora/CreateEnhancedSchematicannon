package com.sshakusora.create_enhanced_schematicannon;

import com.mojang.logging.LogUtils;
import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(com.sshakusora.create_enhanced_schematicannon.CES.MODID)
public class CES
{
    public static final String MODID = "create_enhanced_schematicannon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CES()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CESNetwork::register);
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MODID, path);
    }
}
