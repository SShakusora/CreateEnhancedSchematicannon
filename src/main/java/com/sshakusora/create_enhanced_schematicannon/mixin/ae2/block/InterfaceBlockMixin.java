package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.block;

import appeng.block.misc.InterfaceBlock;
import com.sshakusora.create_enhanced_schematicannon.network.INeedSyncBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InterfaceBlock.class)
public class InterfaceBlockMixin implements INeedSyncBlock {
}
