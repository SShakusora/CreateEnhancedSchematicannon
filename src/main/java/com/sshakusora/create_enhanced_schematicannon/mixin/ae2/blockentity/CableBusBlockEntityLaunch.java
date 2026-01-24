package com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity;

import appeng.api.implementations.parts.ICablePart;
import appeng.api.parts.IFacadeContainer;
import appeng.api.parts.IFacadePart;
import appeng.api.parts.IPart;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import appeng.facade.FacadePart;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.parts.CableBusContainer;
import appeng.parts.automation.AnnihilationPlanePart;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.misc.InterfacePart;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusContainerAccessor;
import com.sshakusora.create_enhanced_schematicannon.mixin.ae2.blockentity.accessor.CableBusStorageAccessor;
import com.sshakusora.create_enhanced_schematicannon.util.MapDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SchematicannonBlockEntity.class)
public class CableBusBlockEntityLaunch {
    @Inject(method = "launchBlockOrBelt", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/cannon/SchematicannonBlockEntity;launchBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 1),locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void inject$modifyNBT(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity, CallbackInfo ci, CompoundTag data) {
        if (!(blockEntity instanceof CableBusBlockEntity self)) return;

        SchematicannonBlockEntity SBEntity = (SchematicannonBlockEntity)(Object)this;
        ItemStack blueprint = SBEntity.inventory.getStackInSlot(0);

        if (!blueprint.hasTag()) return;

        CompoundTag printTag = blueprint.getOrCreateTag();
        Rotation rotation = Rotation.NONE;
        Mirror mirror = Mirror.NONE;

        if (printTag.contains("Rotation")) rotation = Rotation.valueOf(printTag.getString("Rotation"));
        if (printTag.contains("Mirror")) mirror = Mirror.valueOf(printTag.getString("Mirror"));

        CompoundTag tag = new CompoundTag();
        CableBusBlockEntity newCable = new CableBusBlockEntity(AEBlockEntities.CABLE_BUS, self.getBlockPos(), self.getBlockState());
        CableBusContainer newCb = newCable.getCableBus();
        CableBusContainerAccessor newCbAc = (CableBusContainerAccessor) newCb;
        IFacadeContainer oldFacades = self.getFacadeContainer();
        IFacadeContainer newFacades = newCable.getFacadeContainer();

        for (Direction dir : Direction.values()) {
            IPart part = self.getPart(dir);
            Direction mapped = MapDirection.mapDir(rotation, mirror, dir);
            if(part != null) {
                if(part instanceof InterfacePart interfacePart) {
                    GenericStackInv storage = interfacePart.getStorage();
                    storage.clear();
                } else if(part instanceof PatternProviderPart patternProviderPart) {
                    GenericStackInv storage = patternProviderPart.getLogic().getReturnInv();
                    storage.clear();
                } else if(part instanceof AnnihilationPlanePart annihilationPlanePart) {
                    annihilationPlanePart.readFromNBT(new CompoundTag());
                }
                ((CableBusStorageAccessor) newCbAc.getStorage()).invokeSetPart(mapped, part);
            }

            IFacadePart f = oldFacades.getFacade(dir);
            if (f != null) {
                newFacades.addFacade(new FacadePart(f.getItemStack(), mapped));
            }
        }

        IPart part = self.getPart(null);
        if(part != null) {
            ((CableBusStorageAccessor) newCbAc.getStorage()).invokeSetCenter((ICablePart) part);
        }

        newCb.updateConnections();
        newCable.saveAdditional(tag);

        data.merge(tag);
    }
}
