package com.sshakusora.create_enhanced_schematicannon;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static class Server {

        public final ModConfigSpec.LongValue maxSchematicVolume;
        public final ModConfigSpec.IntValue maxSchematicEdge;

        Server(ModConfigSpec.Builder builder) {
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
