package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor;

import appeng.api.implementations.parts.ICablePart;
import appeng.api.parts.IPart;
import appeng.parts.CableBusStorage;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CableBusStorage.class)
public interface CableBusStorageAccessor {
    @Invoker("setCenter")
    void invokeSetCenter(ICablePart center);

    @Invoker("setPart")
    void invokeSetPart(Direction side, IPart part);
}
