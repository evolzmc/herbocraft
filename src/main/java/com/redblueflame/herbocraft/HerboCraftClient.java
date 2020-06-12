package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.entities.renderer.TurretBaseRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

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

        ClientSidePacketRegistry.INSTANCE.register(HerboCraftPackets.WATERING_PARTICLE_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();

            packetContext.getTaskQueue().execute(() -> {
                MinecraftClient.getInstance().player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);

                for (int i = 0; i < HerboCraft.WATERING_CAN_MAX_PARTICLES; i++) {
                    for (int j = 0; j < HerboCraft.WATERING_CAN_MAX_PARTICLES; j++) {
                        MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.SPLASH,
                                pos.getX() + ((double) i / (double) (HerboCraft.WATERING_CAN_MAX_PARTICLES - 1)),
                                pos.getY(),
                                pos.getZ() + ((double) j / (double) (HerboCraft.WATERING_CAN_MAX_PARTICLES - 1)),
                                0.0D,
                                0.0D,
                                0.0D);
                    }
                }
            });
        });
    }
}
