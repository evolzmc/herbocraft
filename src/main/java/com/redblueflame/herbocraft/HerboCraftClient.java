package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.renderer.TurretBaseRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class HerboCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(HerboCraft.TURRET_BASE, (dispatcher, context) -> {
            return new TurretBaseRenderer(dispatcher);
        });
        EntityRendererRegistry.INSTANCE.register(HerboCraft.BULLET, (dispatcher, context) -> {
            return new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer());
        });
    }
}
