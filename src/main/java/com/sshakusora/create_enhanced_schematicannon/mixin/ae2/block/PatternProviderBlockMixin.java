package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.crafting.PatternProviderBlock;
import appeng.block.crafting.PushDirection;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import static appeng.block.crafting.PatternProviderBlock.PUSH_DIRECTION;

@Mixin(PatternProviderBlock.class)
public abstract class PatternProviderBlockMixin extends Block {
    public PatternProviderBlockMixin(Properties p_49795_) {super(p_49795_);}

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (state.hasProperty(PUSH_DIRECTION)) {
            PushDirection pushDir = state.getValue(PUSH_DIRECTION);
            if (pushDir == PushDirection.ALL) {
                return state;
            }
            Direction dir = pushDirectionToDirection(pushDir);
            Direction rotated = rotation.rotate(dir);
            PushDirection newPushDir = directionToPushDirection(rotated);
            return state.setValue(PUSH_DIRECTION, newPushDir);
        }
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (state.hasProperty(PUSH_DIRECTION)) {
            PushDirection pushDir = state.getValue(PUSH_DIRECTION);
            if (pushDir == PushDirection.ALL) {
                return state;
            }
            Rotation r = mirror.getRotation(pushDirectionToDirection(pushDir));
            return this.rotate(state, r);
        }
        return state;
    }

    private static Direction pushDirectionToDirection(PushDirection pd) {
        return switch (pd) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
            case ALL -> Direction.UP;
        };
    }

    private static PushDirection directionToPushDirection(Direction d) {
        return switch (d) {
            case DOWN -> PushDirection.DOWN;
            case UP -> PushDirection.UP;
            case NORTH -> PushDirection.NORTH;
            case SOUTH -> PushDirection.SOUTH;
            case WEST -> PushDirection.WEST;
            case EAST -> PushDirection.EAST;
        };
    }
}
