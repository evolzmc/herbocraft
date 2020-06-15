package com.redblueflame.herbocraft;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.redblueflame.herbocraft.blocks.*;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import com.redblueflame.herbocraft.items.TurretAnalyzer;
import com.redblueflame.herbocraft.items.TurretSeed;
import com.redblueflame.herbocraft.items.WateringCanItem;
import com.redblueflame.herbocraft.ui.GrowthControllerInterface;
import com.redblueflame.herbocraft.ui.SterilizerInterface;
import com.redblueflame.herbocraft.utils.TurretLooter;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HerboCraft implements ModInitializer {
    public static final String name = "herbocraft";
    public static final int WATERING_CAN_MAX_PARTICLES = 10;

    // Utils
    public static Gson GSON;
    public static TurretLooter LOOTS;

    public static Tag<Item> SEEDS;
    public static Tag<Item> BASE_SEEDS;
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

    public static final Block STERILIZER = new SterilizerBlock(FabricBlockSettings.of(Material.METAL));
    public static final Block GROWTH_CONTROLLER = new GrowthController(FabricBlockSettings.of(Material.METAL));
    //endregion

    //region Components
    public static final ComponentType<LevelComponent> LEVELLING =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(name, "levelling"), LevelComponent.class);
    //endregion

    //region Block Entities
    public static BlockEntityType<SterilizerBlockEntity> STERILIZER_BLOCK_ENTITY;
    public static BlockEntityType<GrowthControllerBlockEntity> GROWTH_CONTROLLER_BLOCK_ENTITY;
    //endregion

    //region Containers
    public static final Identifier STERILIZER_CONTAINER = new Identifier(name, "sterilizer_interface");
    public static final Identifier GROWTH_CONTROLLER_CONTAINER = new Identifier(name, "growth_controller_interface");
    //endregion
    @Override
    public void onInitialize() {
        // Register GSON
        GSON = new Gson();
        // Register Items
        FabricDefaultAttributeRegistry.register(TURRET_BASE, TurretBaseEntity.createBaseTurretAttributes());
        Registry.register(Registry.ITEM, new Identifier(name, "watering_can"), WATERING_CAN);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_seed"), TURRET_SEED);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_analyser"), TURRET_ANALYSER);
        SEEDS = TagRegistry.item(new Identifier(name, "seed"));
        BASE_SEEDS = TagRegistry.item(new Identifier(name, "base_seed"));
        // Register blocks
        Registry.register(Registry.BLOCK, new Identifier(name, "sterilizer"), STERILIZER);
        Registry.register(Registry.ITEM, new Identifier(name, "sterilizer"), new BlockItem(STERILIZER, new Item.Settings().group(HERBO_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(name, "growth_controller"), GROWTH_CONTROLLER);
        Registry.register(Registry.ITEM, new Identifier(name, "growth_controller"), new BlockItem(GROWTH_CONTROLLER, new Item.Settings().group(HERBO_GROUP)));
        // Register blockentities
        STERILIZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "sterilizer"), BlockEntityType.Builder.create(SterilizerBlockEntity::new, STERILIZER).build(null));
        GROWTH_CONTROLLER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "growth_controller"), BlockEntityType.Builder.create(GrowthControllerBlockEntity::new, GROWTH_CONTROLLER).build(null));
        // Register containers
        ContainerProviderRegistry.INSTANCE.registerFactory(STERILIZER_CONTAINER,
                (syncId, id, player, buf) -> new SterilizerBlockContainer(syncId, buf.readText(), player.inventory, buf.readBlockPos(), player.world));
        ScreenProviderRegistry.INSTANCE.registerFactory(STERILIZER_CONTAINER, SterilizerInterface::new);
        ContainerProviderRegistry.INSTANCE.registerFactory(GROWTH_CONTROLLER_CONTAINER,
                (syncId, id, player, buf) -> new GrowthControllerContainer(syncId, buf.readText(), player.inventory, buf.readBlockPos(), player.world));
        ScreenProviderRegistry.INSTANCE.registerFactory(GROWTH_CONTROLLER_CONTAINER, GrowthControllerInterface::new);
        // region Packets
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
        //endregion
        // Register turret Levelling system
        EntityComponentCallback.event(TurretBaseEntity.class).register(TurretBaseEntity::initComponents);
        ItemComponentCallback.event(TURRET_SEED).register((stack, components) -> components.put(LEVELLING, TurretLevelComponent.getRandomStats((short) 5)));
        // Load JSON config files
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(name, "data");
            }

            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        InputStream stream = manager.getResource(new Identifier(name, "config/growth_controller_drops.json5")).getInputStream();
                        String result = new BufferedReader(new InputStreamReader(stream))
                                .lines().collect(Collectors.joining("\n"));
                        LOOTS = GSON.fromJson(result, TurretLooter.class);
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }, prepareExecutor).thenCompose(avoid ->
                    synchronizer.whenPrepared(null)
                );
            }
        });
    }

}
