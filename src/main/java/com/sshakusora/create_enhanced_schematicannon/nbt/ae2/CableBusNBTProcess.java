package com.sshakusora.create_enhanced_schematicannon.nbt.ae2;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Map;
import java.util.function.Function;

public class CableBusNBTProcess {
    private CompoundTag rotateCableBusNBT(CompoundTag original, Function<Direction, Direction> mapper) {
        CompoundTag result = original.copy();

        for (Direction dir : Direction.values()) {
            String key = dir.getName();
            if (!original.contains(key, Tag.TAG_COMPOUND))
                continue;

            Direction newDir = mapper.apply(dir);
            String newKey = newDir.getName();

            result.remove(key);
            result.put(newKey, original.getCompound(key));
        }

        return result;
    }

    private static final Map<Direction, Direction> ROTATE_CW_90 = Map.of(
            Direction.NORTH, Direction.EAST,
            Direction.EAST,  Direction.SOUTH,
            Direction.SOUTH, Direction.WEST,
            Direction.WEST,  Direction.NORTH,
            Direction.UP,    Direction.UP,
            Direction.DOWN,  Direction.DOWN
    );
}
