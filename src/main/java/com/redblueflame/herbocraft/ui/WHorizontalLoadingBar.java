package com.redblueflame.herbocraft.ui;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.widget.WStaticImage;
import spinnery.widget.api.Color;

public class WHorizontalLoadingBar extends WStaticImage {
    private short state = 0;
    public void setState(short i) {
        state = i;
    }
    public short getState() {
        return state;
    }

    @Override
    public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
        if (!this.isHidden()) {
            float x = this.getX();
            float y = this.getY();
            float z = this.getZ();
            float sX = this.getWidth()*(state/255F);
            float sY = this.getHeight();
            BaseRenderer.drawTexturedQuad(matrices, provider, x, y, z, sX, sY, 0F, 0F, state/255F, 1F, 15728880, new Color(255, 255, 255, 255), this.getTexture());
        }
    }
}
