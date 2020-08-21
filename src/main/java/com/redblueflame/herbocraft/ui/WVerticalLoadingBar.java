package com.redblueflame.herbocraft.ui;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.render.BaseRenderer;
import spinnery.widget.WStaticImage;
import spinnery.widget.api.Color;

public class WVerticalLoadingBar extends WStaticImage {
    private short state = 0;
    public void setState(short i) {
        state = i;
    }
    public short getState() {
        return state;
    }

    @Override
    public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
        if (!this.isHidden()) {
            float height = this.getHeight() * (state / 255F);
            float x = this.getX();
            float y = this.getY() + (this.getHeight() - height);
            float z = this.getZ();
            float sX = this.getWidth();
            @SuppressWarnings("UnnecessaryLocalVariable")
            float sY = height;
            BaseRenderer.drawTexturedQuad(matrices, provider, x, y, z, sX, sY, 0F, 1-(state/255F), 1F, 1F, 15728880, new Color(255, 255, 255, 255), this.getTexture());
        }
    }
}
