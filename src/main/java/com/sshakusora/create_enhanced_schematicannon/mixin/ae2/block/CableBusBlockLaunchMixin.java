package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.networking.CableBusBlock;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.cannon.LaunchedItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(LaunchedItem.ForBlockState.class)
public class CableBusBlockLaunchMixin {
    @Inject(method = "place", at = @At("HEAD"), remap = false)
    private void cableBusBlockLaunchFix(Level world, CallbackInfo ci) {
        LaunchedItem.ForBlockState self = (LaunchedItem.ForBlockState) (Object) this;

        if (!(self.state.getBlock() instanceof CableBusBlock)) return;

        StructurePlaceSettings settings = CreateClient.SCHEMATIC_HANDLER.getTransformation().toSettings();
        Mirror mirror = settings.getMirror();
        Rotation rotation = settings.getRotation();
        CompoundTag data = self.data;

        if (mirror != Mirror.NONE) {
            data = remapDirections(data, d -> mirror(mirror, d));
        }

        if (rotation != Rotation.NONE) {
            data = remapDirections(data, d -> rotation.rotate(d));
        }

        self.data = data;
    }

    private CompoundTag remapDirections(CompoundTag tag, Function<Direction, Direction> mapper) {
        CompoundTag newTag = new CompoundTag();

        for (String key : tag.getAllKeys()) {
            if (!isDirectionKey(key)) {
                newTag.put(key, tag.get(key));
            }
        }

        for (Direction dir : Direction.values()) {
            String key = dir.getName();
            if (tag.contains(key, 10)) {
                CompoundTag part = tag.getCompound(key);
                Direction newDir = mapper.apply(dir);
                newTag.put(newDir.getName(), part);
            }
        }

        if (tag.contains("cable", 10)) {
            CompoundTag cable = tag.getCompound("cable").copy();
            if (cable.contains("visual", 10)) {
                CompoundTag visual = cable.getCompound("visual").copy();
                if (visual.contains("connections", 9)) {
                    ListTag connections = visual.getList("connections", 8);
                    ListTag newConnections = new ListTag();
                    for (int i = 0; i < connections.size(); i++) {
                        String oldDir = connections.getString(i);
                        Direction d = Direction.byName(oldDir);
                        if (d != null) {
                            Direction newDir = mapper.apply(d);
                            newConnections.add(StringTag.valueOf(newDir.getName()));
                        } else {
                            newConnections.add(StringTag.valueOf(oldDir));
                        }
                    }
                    visual.put("connections", newConnections);
                }
                cable.put("visual", visual);
            }
            newTag.put("cable", cable);
        }

        return newTag;
    }

    private boolean isDirectionKey(String key) {
        return key.equals("north") || key.equals("south") || key.equals("east") || key.equals("west") || key.equals("up") || key.equals("down");
    }

    private Direction mirror(Mirror mirror, Direction dir) {
        if (mirror == Mirror.LEFT_RIGHT) {
            return switch (dir) {
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                default -> dir;
            };
        } else if (mirror == Mirror.FRONT_BACK) {
            return switch (dir) {
                case EAST -> Direction.WEST;
                case WEST -> Direction.EAST;
                default -> dir;
            };
        }
        return dir;
    }
}
