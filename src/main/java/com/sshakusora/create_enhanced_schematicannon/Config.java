package com.sshakusora.create_enhanced_schematicannon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CES.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static class Server {

        public final ForgeConfigSpec.LongValue maxSchematicVolume;
        public final ForgeConfigSpec.IntValue maxSchematicEdge;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("schematic");

            maxSchematicVolume = builder
                    .comment("Maximum allowed schematic volume (X * Y * Z)")
                    .defineInRange(
                            "maxVolume",
                            500_000L,
                            1L,
                            10_000_000L
                    );

            maxSchematicEdge = builder
                    .comment("Maximum allowed schematic edge length (X/Y/Z)")
                    .defineInRange(
                            "maxEdge",
                            256,
                            1,
                            1024
                    );

            builder.pop();
        }
    }
}
