package com.redblueflame.herbocraft.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class EntitySpawningHandlingMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;getEntityTypeId()Lnet/minecraft/entity/EntityType;"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void handleEntitySpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo ctx, double x, double y, double z, EntityType<?> type) {
        Identifier id = Registry.ENTITY_TYPE.getId(type);
        if (id.getNamespace().equals("herbocraft")) {
            Entity entity = type.create(world);
            if (entity == null) {
                ctx.cancel();
                return;
            }

            int networkId = packet.getId();
            entity.updateTrackedPosition(x, y, z);
            entity.pitch = (float) (packet.getPitch() * 360) / 256.0F;
            entity.yaw = (float) (packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(networkId);
            entity.setUuid(packet.getUuid());
            this.world.addEntity(networkId, entity);
            ctx.cancel();
        }
    }
}
