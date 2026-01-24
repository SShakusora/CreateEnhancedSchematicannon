package com.sshakusora.create_enhanced_schematicannon.sync.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.Indicator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class VerticalIndicator extends AbstractSimiWidget {
    public Indicator.State state;

    public VerticalIndicator(int x, int y, Component tooltip) {
        super(x, y, AllGuiTextures.INDICATOR.height, AllGuiTextures.INDICATOR.width);
        this.toolTip = this.toolTip.isEmpty() ? ImmutableList.of() : ImmutableList.of(tooltip);
        this.state = Indicator.State.OFF;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        AllGuiTextures toDraw = switch (state) {
            case ON -> AllGuiTextures.INDICATOR_WHITE;
            case OFF -> AllGuiTextures.INDICATOR;
            case RED -> AllGuiTextures.INDICATOR_RED;
            case YELLOW -> AllGuiTextures.INDICATOR_YELLOW;
            case GREEN -> AllGuiTextures.INDICATOR_GREEN;
        };

        graphics.pose().pushPose();

        graphics.pose().translate(getX() + this.width / 2f, getY() + this.height / 2f, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(-90));
        graphics.pose().translate(-toDraw.width / 2f, -toDraw.height / 2f, 0);
        toDraw.render(graphics, 0, 0);

        graphics.pose().popPose();
    }
}
