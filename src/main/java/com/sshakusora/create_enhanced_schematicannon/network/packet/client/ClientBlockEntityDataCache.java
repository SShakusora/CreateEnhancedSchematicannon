package com.sshakusora.create_enhanced_schematicannon.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class ClientBlockEntityDataCache {
    private static final Map<BlockPos, CompoundTag> CACHE = new HashMap<>();

    public static void put(BlockPos pos, CompoundTag tag) {
        System.out.println("Putting " + pos + " and " + tag +" in cache");
        CACHE.put(pos, tag);
    }

    public static CompoundTag get(BlockPos pos) {
        System.out.println("Getting " + pos + " from cache");
        return CACHE.get(pos);
    }

    public static void clear() {
        System.out.println("Clearing block entity data cache");
        CACHE.clear();
    }
}

