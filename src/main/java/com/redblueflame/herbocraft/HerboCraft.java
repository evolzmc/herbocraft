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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HerboCraft implements ModInitializer {
    public static final String name = "herbocraft";
    public static final int WATERING_CAN_MAX_PARTICLES = 2;

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
            int range = packetByteBuf.readInt();
            List<BlockPos> positions = new ArrayList<>();

            for (int x = 0; x < range; x++) for (int z = 0; z < range; z++) if (!contains(positions, pos.getX() + x, pos.getZ() + z)) positions.add(pos.add(x, 0, z));

            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                World world = player.world;

                for (BlockPos blockPos : positions) {
                    BlockState state = world.getBlockState(blockPos);
                    Block block = state.getBlock();
                    Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, blockPos);

                    if (block instanceof CropBlock) {
                        CropBlock cropBlock = (CropBlock) block;

                        if (!cropBlock.isMature(state)) {
                            ItemStack inHandStack = hand.equals(Hand.MAIN_HAND) ? player.getMainHandStack() : player.getOffHandStack();
                            if (inHandStack.getDamage() >= inHandStack.getMaxDamage()) return;
                            inHandStack.damage(1, world.random, player);

                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeBlockPos(blockPos);
                            watchingPlayers.forEach(players -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(players, HerboCraftPackets.WATERING_PARTICLE_PACKET, passedData));

                            cropBlock.randomTick(state, (ServerWorld) world, blockPos, world.random);
                        }
                    }
                }
            });
        });
    }

    private boolean contains(List<BlockPos> positions, double x, double z) {
        for (BlockPos pos : positions) if (pos.getX() == x && pos.getZ() == z) return true;
        return false;
    }
}
