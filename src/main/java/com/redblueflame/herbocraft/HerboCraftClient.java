package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.blocks.AbstractProgressBlockEntity;
import com.redblueflame.herbocraft.entities.renderer.SnowTurretRenderer;
import com.redblueflame.herbocraft.entities.renderer.TurretBaseRenderer;
import com.redblueflame.herbocraft.entities.renderer.WitherTurretRenderer;
import com.redblueflame.herbocraft.ui.GrowthControllerInterface;
import com.redblueflame.herbocraft.ui.ReproducerBlockInterface;
import com.redblueflame.herbocraft.ui.SterilizerInterface;
import com.redblueflame.herbocraft.ui.UpgraderBlockInterface;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class HerboCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(HerboCraft.STERILIZER_CONTAINER, SterilizerInterface::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(HerboCraft.GROWTH_CONTROLLER_CONTAINER, GrowthControllerInterface::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(HerboCraft.REPRODUCER_CONTAINER, ReproducerBlockInterface::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(HerboCraft.UPGRADER_CONTAINER, UpgraderBlockInterface::new);
        EntityRendererRegistry.INSTANCE.register(HerboCraft.TURRET_BASE, (dispatcher, context) -> new TurretBaseRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(HerboCraft.BULLET, (dispatcher, context) -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer()));
        EntityRendererRegistry.INSTANCE.register(HerboCraft.SNOW_TURRET, (entityRenderDispatcher, context) -> new SnowTurretRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(HerboCraft.WITHER_TURRET, (entityRenderDispatcher, context) -> new WitherTurretRenderer(entityRenderDispatcher));
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.TURRET_SEED_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.WITHER_SEED_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.SNOW_SEED_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.STERILIZER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.REPRODUCER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HerboCraft.MACHINE_FRAME, RenderLayer.getCutout());
        ClientSidePacketRegistry.INSTANCE.register(HerboCraftPackets.WATERING_PARTICLE_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();

            packetContext.getTaskQueue().execute(() -> {
                assert MinecraftClient.getInstance().player != null;
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
        // Register the initial load
        ClientSidePacketRegistry.INSTANCE.register(HerboCraftPackets.STATE_CHANGE, ((packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            boolean isWorking = packetByteBuf.readBoolean();
            int targetWork = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                assert MinecraftClient.getInstance().world != null;
                AbstractProgressBlockEntity blockEntity = (AbstractProgressBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
                if (blockEntity == null) {
                    HerboCraft.LOGGER.error("The block entity you're trying to change does not exist on the client !");
                    return;
                }
                if (isWorking) {
                    blockEntity.isWorking = true;
                    blockEntity.targetWork = targetWork;
                } else {
                    blockEntity.resetWork();
                }
            });
        }));
        ClientSidePacketRegistry.INSTANCE.register(HerboCraftPackets.SEND_STATUS, ((packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            int targetWork = packetByteBuf.readInt();
            int currentWork = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                assert MinecraftClient.getInstance().world != null;
                AbstractProgressBlockEntity blockEntity = (AbstractProgressBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
                if (blockEntity == null) {
                    HerboCraft.LOGGER.error("The block entity you're trying to change does nos exist on the client !");
                    return;
                }
                blockEntity.targetWork = targetWork;
                blockEntity.currentWork = currentWork;
                if (targetWork != 0) {
                    blockEntity.state = (int) ((targetWork/(float)currentWork)*255F);
                }
            });
        }));
    }
}
