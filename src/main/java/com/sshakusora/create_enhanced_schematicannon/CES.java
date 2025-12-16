package com.sshakusora.create_enhanced_schematicannon;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(com.sshakusora.create_enhanced_schematicannon.CES.MODID)
public class CES
{
    public static final String MODID = "create_enhanced_schematicannon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CES()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
