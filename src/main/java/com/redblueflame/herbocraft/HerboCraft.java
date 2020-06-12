package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class HerboCraft implements ModInitializer {
    public static final String name = "herbocraft";
    public static final int WATERING_CAN_MAX_PARTICLES = 10;

    /**
     * Registers the turret with the ID "herbocraft:turret_base"
     * <p>
     * This entity is the standard turret, that shoots projectiles
     */
    public static final EntityType<TurretBaseEntity> TURRET_BASE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(name, "turret_base"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TurretBaseEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<BulletEntity> BULLET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(name, "bullet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BulletEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(TURRET_BASE, TurretBaseEntity.createBaseTurretAttributes());

        /*
        Items
         */
        Registry.register(Registry.ITEM, new Identifier(name, "watering_can"), HerboCraftItems.WATERING_CAN);

        /*
        Server side packet
         */
        ServerSidePacketRegistry.INSTANCE.register(HerboCraftPackets.WATERING_CAN_USAGE_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            Hand hand = packetByteBuf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;

            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                World world = player.world;
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, pos);

                if (block instanceof CropBlock) {
                    CropBlock cropBlock = (CropBlock) block;

                    if (!cropBlock.isMature(state)) {
                        ItemStack inHandStack = hand.equals(Hand.MAIN_HAND) ? player.getMainHandStack() : player.getOffHandStack();
                        if (inHandStack.getDamage() >= inHandStack.getMaxDamage()) return;
                        inHandStack.damage(1, world.random, player);

                        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                        passedData.writeBlockPos(pos);
                        watchingPlayers.forEach(players -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(players, HerboCraftPackets.WATERING_PARTICLE_PACKET, passedData));

                        cropBlock.randomTick(state, (ServerWorld) world, pos, world.random);
                    }
                }
            });
        });
    }
}
