package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor;

import appeng.parts.CableBusContainer;
import appeng.parts.CableBusStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CableBusContainer.class)
public interface CableBusContainerAccessor {
    @Accessor("storage")
    CableBusStorage getStorage();
}
