package com.sshakusora.create_enhanced_schematicannon.sync.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sshakusora.create_enhanced_schematicannon.CES;
import net.createmod.catnip.gui.element.ScreenElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SyncIcon implements ScreenElement {
    public final ResourceLocation SYNC_ICON = CES.rl("textures/gui/sync_icon.png");
    public static final SyncIcon I_SYNC = new SyncIcon();

    public void render(GuiGraphics graphics, int x, int y) {
        RenderSystem.setShaderTexture(0, SYNC_ICON);

        graphics.blit(
                SYNC_ICON,
                x, y,
                0,
                0, 0,
                16, 16,
                16, 16
        );
    }
}
