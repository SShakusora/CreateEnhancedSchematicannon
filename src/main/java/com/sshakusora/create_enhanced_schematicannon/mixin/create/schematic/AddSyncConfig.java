package com.sshakusora.create_enhanced_schematicannon.mixin.create.schematic;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.SchematicAndQuillHandler;
import com.simibubi.create.content.schematics.client.SchematicPromptScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.sshakusora.create_enhanced_schematicannon.network.CESNetwork;
import com.sshakusora.create_enhanced_schematicannon.network.packet.client.RequestBlockEntityDataPacket;
import com.sshakusora.create_enhanced_schematicannon.sync.gui.SyncIcon;
import com.sshakusora.create_enhanced_schematicannon.sync.gui.VerticalIndicator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(SchematicPromptScreen.class)
public class AddSyncConfig extends Screen {
    @Shadow private EditBox nameField;

    @Unique private final Component syncLabel = Component.translatable("create_enhanced_schematicannon.action.sync");
    @Unique private final Component serverNotLoadedLabel = Component.translatable("create_enhanced_schematicannon.action.sync.server_not_loaded").withStyle(ChatFormatting.RED);
    @Unique private IconButton syncButton;
    @Unique private VerticalIndicator syncIndicator;
    @Unique private final Component optionEnabled = Lang.translateDirect("gui.schematicannon.optionEnabled", new Object[0]);
    @Unique private final Component optionDisabled = Lang.translateDirect("gui.schematicannon.optionDisabled", new Object[0]);

    @Unique
    protected boolean enableSync() {
        return this.syncIndicator.state == Indicator.State.ON;
    }

    @Unique
    private boolean serverLoaded() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener == null) return false;

        Connection connection = listener.getConnection();
        return CESNetwork.CHANNEL.isRemotePresent(connection);
    }

    @Unique
    private void fillToolTip(IconButton button, String tooltipKey) {
        if (button.isHovered()) {
            List<Component> tip = button.getToolTip();
            tip.add((this.enableSync() ? this.optionEnabled : this.optionDisabled).plainCopy().withStyle(ChatFormatting.BLUE));
            tip.addAll(TooltipHelper.cutTextComponent(Component.translatable(tooltipKey + ".description"), TooltipHelper.Palette.ALL_GRAY));
        }
    }

    private AddSyncConfig(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.syncButton == null || this.syncIndicator == null) return;

        this.syncButton.setToolTip(this.syncButton.getToolTip().get(0));
        if (!this.serverLoaded()) {
            this.syncButton.getToolTip().add(this.serverNotLoadedLabel);
        }
        this.syncButton.getToolTip().add(TooltipHelper.holdShift(TooltipHelper.Palette.BLUE, hasShiftDown()));

        if (hasShiftDown()) {
            this.fillToolTip(this.syncButton, "create_enhanced_schematicannon.action.sync");
        }
    }

    @Inject(method = "init", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addSyncConfig(CallbackInfo ci, int x, int y) {
        this.syncButton = new IconButton(x + 29, y + 53, SyncIcon.I_SYNC);
        this.syncIndicator = new VerticalIndicator(x + 47, y + 53, Components.immutableEmpty());

        if (this.serverLoaded()) {
            this.syncButton.withCallback(() -> this.syncIndicator.state = this.enableSync() ? Indicator.State.OFF : Indicator.State.ON);
        } else {
            this.syncIndicator.state = Indicator.State.RED;
        }

        this.syncButton.setToolTip(this.syncLabel);
        this.addRenderableWidget(this.syncButton);
        this.addRenderableWidget(this.syncIndicator);
    }

    @Inject(method = "confirm", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSyncConfirm(boolean convertImmediately, CallbackInfo ci) {
        if (!this.serverLoaded() || !this.enableSync()) return;

        SchematicAndQuillHandler handler = CreateClient.SCHEMATIC_AND_QUILL_HANDLER;
        CESNetwork.CHANNEL.sendToServer(new RequestBlockEntityDataPacket(this.nameField.getValue(), convertImmediately, handler.firstPos, handler.secondPos));
        this.onClose();

        ci.cancel();
    }
}
