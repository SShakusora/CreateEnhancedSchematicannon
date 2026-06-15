package com.sshakusora.create_enhanced_schematicannon.mixin.functionalstorage.schematic;

import com.buuz135.functionalstorage.block.tile.ControllableDrawerTile;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.sshakusora.create_enhanced_schematicannon.util.functionalstorage.FunctionalStoragePrintContext;
import net.createmod.catnip.levelWrappers.SchematicLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchematicPrinter.class)
public abstract class SchematicPrinterMixin {
    @Shadow(remap = false) private SchematicLevel blockReader;

    @Shadow(remap = false) private SchematicPrinter.PrintStage printStage;

    @Shadow(remap = false) public abstract BlockPos getCurrentTarget();

    @Inject(method = "getCurrentRequirement", at = @At("HEAD"), cancellable = true, remap = false)
    private void create_enhanced_schematicannon$getFunctionalStorageRequirement(CallbackInfoReturnable<ItemRequirement> cir) {
        if (this.printStage == SchematicPrinter.PrintStage.ENTITIES || this.blockReader == null) return;

        BlockPos target = this.getCurrentTarget();
        BlockState state = BlockHelper.setZeroAge(this.blockReader.getBlockState(target));
        BlockEntity blockEntity = this.blockReader.getBlockEntity(target);
        if (!(blockEntity instanceof ControllableDrawerTile)) return;

        if (FunctionalStoragePrintContext.isSkippingMissing()) {
            cir.setReturnValue(ItemRequirement.of(state, null));
            return;
        }

        cir.setReturnValue(ItemRequirement.of(state, blockEntity));
    }
}
