package com.sshakusora.create_enhanced_schematicannon.mixin.create.schematic;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.SchematicAndQuillHandler;
import com.simibubi.create.content.schematics.client.SchematicPromptScreen;
import com.simibubi.create.foundation.gui.AllIcons;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
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

    @Unique private SchematicPromptScreen self = (SchematicPromptScreen) (Object) this;
    @Unique private final Component syncLabel = Lang.translateDirect("action.sync");
    @Unique private IconButton syncButton;
    @Unique private VerticalIndicator syncIndicator;
    @Unique private final Component optionEnabled = Lang.translateDirect("gui.schematicannon.optionEnabled", new Object[0]);
    @Unique private final Component optionDisabled = Lang.translateDirect("gui.schematicannon.optionDisabled", new Object[0]);

    @Unique
    protected boolean enableSync() {
        return this.syncIndicator.state == Indicator.State.ON;
    }

    @Unique
    private void fillToolTip(IconButton button, VerticalIndicator indicator, String tooltipKey) {
        if (button.isHovered()) {
            boolean enabled = indicator.state == Indicator.State.ON;
            List<Component> tip = button.getToolTip();
            tip.add((enabled ? this.optionEnabled : this.optionDisabled).plainCopy().withStyle(ChatFormatting.BLUE));
            tip.addAll(TooltipHelper.cutTextComponent(Lang.translateDirect(tooltipKey + ".description", new Object[0]), TooltipHelper.Palette.ALL_GRAY));
        }
    }

    private AddSyncConfig(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        this.syncButton.setToolTip(this.syncButton.getToolTip().get(0));
        this.syncButton.getToolTip().add(TooltipHelper.holdShift(TooltipHelper.Palette.BLUE, hasShiftDown()));

        if (hasShiftDown()) {
            fillToolTip(this.syncButton, this.syncIndicator, "action.sync");
        }
    }

    @Inject(method = "init", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addSyncConfig(CallbackInfo ci, int x, int y) {
        this.syncButton = new IconButton(x + 29, y + 53, SyncIcon.I_SYNC);
        this.syncButton.withCallback(() -> this.syncIndicator.state = this.enableSync() ? Indicator.State.OFF : Indicator.State.ON);
        this.syncButton.setToolTip(this.syncLabel);
        this.addRenderableWidget(this.syncButton);
        this.syncIndicator = new VerticalIndicator(x + 47, y + 53, Components.immutableEmpty());
        this.addRenderableWidget(this.syncIndicator);
    }

    @Inject(method = "confirm", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSyncConfirm(boolean convertImmediately, CallbackInfo ci) {
        if (!this.enableSync()) return;

        SchematicAndQuillHandler handler = CreateClient.SCHEMATIC_AND_QUILL_HANDLER;
        CESNetwork.CHANNEL.sendToServer(new RequestBlockEntityDataPacket(this.nameField.getValue(), convertImmediately, handler.firstPos, handler.secondPos));
        this.onClose();

        ci.cancel();
    }
}
