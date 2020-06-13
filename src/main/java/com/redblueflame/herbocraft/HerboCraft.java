package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import com.redblueflame.herbocraft.items.TurretAnalyzer;
import com.redblueflame.herbocraft.items.TurretSeed;
import com.redblueflame.herbocraft.items.WateringCanItem;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class HerboCraft implements ModInitializer {
    public static final String name = "herbocraft";
    public static final int WATERING_CAN_MAX_PARTICLES = 10;

    public static final Identifier WATERING_CAN_USAGE_PACKET = new Identifier(name, "watering_can_usage");
    public static final Identifier WATERING_PARTICLE_PACKET = new Identifier(name, "watering_particle");

    /**
     * Registers the turret with the ID "herbocraft:turret_base"
     * <p>
     * This entity is the standard turret, that shoots projectiles
     */
    //region Entities
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
    //endregion

    //region Items
    public static final ItemGroup HERBO_GROUP = FabricItemGroupBuilder.create(new Identifier(name, "herbo_group")).icon(() -> new ItemStack(Items.GHAST_TEAR)).build();
    public static final Item WATERING_CAN = new WateringCanItem(new Item.Settings().group(HERBO_GROUP).maxCount(1).maxDamage(10));
    public static final Item TURRET_SEED = new TurretSeed(new Item.Settings().group(HERBO_GROUP).maxCount(64));
    public static final Item TURRET_ANALYSER = new TurretAnalyzer(new Item.Settings().group(HERBO_GROUP).maxCount(1));
    //endregion

    //region Components
    public static final ComponentType<LevelComponent> LEVELLING =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier("herbocraft", "levelling"), LevelComponent.class);
    //endregion
    @Override
    public void onInitialize() {
        // Register Items
        FabricDefaultAttributeRegistry.register(TURRET_BASE, TurretBaseEntity.createBaseTurretAttributes());
        Registry.register(Registry.ITEM, new Identifier(name, "watering_can"), WATERING_CAN);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_seed"), TURRET_SEED);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_analyser"), TURRET_ANALYSER);

        ServerSidePacketRegistry.INSTANCE.register(WATERING_CAN_USAGE_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            ItemStack stack = packetByteBuf.readItemStack();

            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                World world = player.world;
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, pos);

                if (block instanceof CropBlock) {
                    CropBlock cropBlock = (CropBlock) block;

                    if (!cropBlock.isMature(state)) {
                        cropBlock.randomTick(state, (ServerWorld) world, pos, world.random);
                        stack.damage(10, world.random, player);

                        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                        passedData.writeBlockPos(pos);
                        watchingPlayers.forEach(players -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(players, WATERING_PARTICLE_PACKET, passedData));
                    }
                }
            });
        });

        // Register turret Levelling system
        EntityComponentCallback.event(TurretBaseEntity.class).register(TurretBaseEntity::initComponents);
        ItemComponentCallback.event(TURRET_SEED).register((stack, components) -> components.put(LEVELLING, TurretLevelComponent.getRandomStats((short) 5)));
    }

}
