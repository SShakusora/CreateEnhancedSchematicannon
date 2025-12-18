package com.sshakusora.create_enhanced_schematicannon.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

import javax.annotation.Nullable;

public class MapDirection {
    public static Direction mapDir(Rotation rotate, Mirror mirror, Direction dir) {
        Direction afterMirror = mirror != Mirror.NONE ? mirror.mirror(dir) : dir;
        return rotate != Rotation.NONE ? rotate.rotate(afterMirror) : afterMirror;
    }

    public static Direction.Axis mapAxis(@Nullable Rotation rotation, @Nullable Mirror mirror, Direction.Axis axis) {
        Direction dir = axisToPositiveDirection(axis);

        if (mirror != null) {
            dir = mirror.mirror(dir);
        }
        if (rotation != null) {
            dir = rotation.rotate(dir);
        }

        return dir.getAxis();
    }

    private static Direction axisToPositiveDirection(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.EAST;
            case Y -> Direction.UP;
            case Z -> Direction.SOUTH;
        };
    }

}
