package com.redblueflame.herbocraft;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.redblueflame.herbocraft.blocks.*;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import com.redblueflame.herbocraft.entities.WitherTurretEntity;
import com.redblueflame.herbocraft.items.TurretAnalyzer;
import com.redblueflame.herbocraft.items.TurretSeed;
import com.redblueflame.herbocraft.items.UpgradeItem;
import com.redblueflame.herbocraft.items.WateringCanItem;
import com.redblueflame.herbocraft.utils.PatchouliBookRecipe;
import com.redblueflame.herbocraft.utils.TurretLooter;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static Logger LOGGER = LogManager.getLogger();

    public static Tag<Item> SEEDS;
    public static Tag<Item> BASE_SEEDS;
    public static Tag<Block> UPGRADABLE_BLOCKS;
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
    public static final EntityType<SnowTurretEntity> SNOW_TURRET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(name, "snow_turret"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnowTurretEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<WitherTurretEntity> WITHER_TURRET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(name, "wither_turret"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WitherTurretEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    //endregion

    //region Items & Blocks
    public static final ItemGroup HERBO_GROUP = FabricItemGroupBuilder.create(new Identifier(name, "herbo_group")).icon(() -> new ItemStack(Items.GHAST_TEAR)).build();
    public static final Item WATERING_CAN = new WateringCanItem(new Item.Settings().group(HERBO_GROUP).maxCount(1).maxDamage(10));

    public static final Item TURRET_ANALYSER = new TurretAnalyzer(new Item.Settings().group(HERBO_GROUP).maxCount(1));
    public static final Item TIER1_UPGRADE = new UpgradeItem(new Item.Settings().group(HERBO_GROUP), QualityType.TIER1);
    public static final Item TIER2_UPGRADE = new UpgradeItem(new Item.Settings().group(HERBO_GROUP), QualityType.TIER2);
    public static final Item TIER3_UPGRADE = new UpgradeItem(new Item.Settings().group(HERBO_GROUP), QualityType.TIER3);
    public static final Item TIER4_UPGRADE = new UpgradeItem(new Item.Settings().group(HERBO_GROUP), QualityType.TIER4);
    public static final Item DIAMOND_GEAR = new Item(new Item.Settings().group(HERBO_GROUP));
    public static final Item DIAMOND_PLATE = new Item(new Item.Settings().group(HERBO_GROUP));
    public static final Item GOLD_GEAR = new Item(new Item.Settings().group(HERBO_GROUP));
    public static final Item GOLD_PLATE = new Item(new Item.Settings().group(HERBO_GROUP));
    public static final Item IRON_GEAR = new Item(new Item.Settings().group(HERBO_GROUP));
    public static final Item IRON_PLATE = new Item(new Item.Settings().group(HERBO_GROUP));

    public static final Block STERILIZER = new SterilizerBlock(FabricBlockSettings.of(Material.METAL));
    public static final Block GROWTH_CONTROLLER = new GrowthController(FabricBlockSettings.of(Material.METAL));
    public static final Block REPRODUCER = new ReproducerBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    public static final Block MACHINE_FRAME = new GlassBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
    public static final Block UPGRADER = new UpgraderBlock(FabricBlockSettings.of(Material.METAL));

    public static final Block TURRET_SEED_BLOCK = new TurretSeedBlock(FabricBlockSettings.of(Material.PLANT));
    public static final Block SNOW_SEED_BLOCK = new SnowTurretSeedBlock(FabricBlockSettings.of(Material.PLANT));
    public static final Block WITHER_SEED_BLOCK = new WitherTurretSeedBlock(FabricBlockSettings.of(Material.PLANT));

    public static Item TURRET_SEED = new TurretSeed(TURRET_SEED_BLOCK, new Item.Settings().group(HERBO_GROUP).maxCount(1));;
    public static Item SNOW_SEED = new TurretSeed(SNOW_SEED_BLOCK, new Item.Settings().group(HERBO_GROUP).maxCount(1));;
    public static Item WITHER_SEED = new TurretSeed(WITHER_SEED_BLOCK, new Item.Settings().group(HERBO_GROUP).maxCount(1));;

    //endregion

    //region Components
    @Deprecated
    public static final ComponentType<LevelComponent> LEVELLING =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(name, "levelling"), LevelComponent.class);
    //endregion

    //region Block Entities
    public static BlockEntityType<SterilizerBlockEntity> STERILIZER_BLOCK_ENTITY;
    public static BlockEntityType<GrowthControllerBlockEntity> GROWTH_CONTROLLER_BLOCK_ENTITY;
    public static BlockEntityType<TurretSeedBlockEntity> TURRET_SEED_BLOCK_ENTITY;
    public static BlockEntityType<ReproducerBlockEntity> REPRODUCER_BLOCK_ENTITY;
    public static BlockEntityType<UpgraderBlockEntity> UPGRADER_BLOCK_ENTITY;
    //endregion

    //region Containers
    public static final Identifier STERILIZER_CONTAINER = new Identifier(name, "sterilizer_interface");
    public static final Identifier GROWTH_CONTROLLER_CONTAINER = new Identifier(name, "growth_controller_interface");
    public static final Identifier REPRODUCER_CONTAINER = new Identifier(name, "reproducer_interface");
    public static final Identifier UPGRADER_CONTAINER = new Identifier(name, "upgrader_interface");
    //endregion
    @Override
    public void onInitialize() {
        // Register GSON
        GSON = new Gson();

        // Register entities
        FabricDefaultAttributeRegistry.register(TURRET_BASE, TurretBaseEntity.createBaseTurretAttributes());
        FabricDefaultAttributeRegistry.register(SNOW_TURRET, TurretBaseEntity.createBaseTurretAttributes());
        FabricDefaultAttributeRegistry.register(WITHER_TURRET, TurretBaseEntity.createBaseTurretAttributes());
        // Register Items
        Registry.register(Registry.ITEM, new Identifier(name, "watering_can"), WATERING_CAN);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_analyser"), TURRET_ANALYSER);
        Registry.register(Registry.ITEM, new Identifier(name, "tier1_upgrade"), TIER1_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(name, "tier2_upgrade"), TIER2_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(name, "tier3_upgrade"), TIER3_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(name, "tier4_upgrade"), TIER4_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(name, "diamond_gear"), DIAMOND_GEAR);
        Registry.register(Registry.ITEM, new Identifier(name, "diamond_plate"), DIAMOND_PLATE);
        Registry.register(Registry.ITEM, new Identifier(name, "gold_gear"), GOLD_GEAR);
        Registry.register(Registry.ITEM, new Identifier(name, "gold_plate"), GOLD_PLATE);
        Registry.register(Registry.ITEM, new Identifier(name, "iron_gear"), IRON_GEAR);
        Registry.register(Registry.ITEM, new Identifier(name, "iron_plate"), IRON_PLATE);

        // Get tags from registry
        SEEDS = TagRegistry.item(new Identifier(name, "seed"));
        BASE_SEEDS = TagRegistry.item(new Identifier(name, "base_seed"));
        UPGRADABLE_BLOCKS = TagRegistry.block(new Identifier(name, "upgradable_blocks"));

        // Register blocks
        Registry.register(Registry.BLOCK, new Identifier(name, "sterilizer"), STERILIZER);
        Registry.register(Registry.ITEM, new Identifier(name, "sterilizer"), new BlockItem(STERILIZER, new Item.Settings().group(HERBO_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(name, "growth_controller"), GROWTH_CONTROLLER);
        Registry.register(Registry.ITEM, new Identifier(name, "growth_controller"), new BlockItem(GROWTH_CONTROLLER, new Item.Settings().group(HERBO_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(name, "turret_seed"), TURRET_SEED_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(name, "turret_seed"), TURRET_SEED);

        Registry.register(Registry.BLOCK, new Identifier(name, "snow_seed"), SNOW_SEED_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(name, "snow_seed"), SNOW_SEED);

        Registry.register(Registry.BLOCK, new Identifier(name, "wither_seed"), WITHER_SEED_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(name, "wither_seed"), WITHER_SEED);

        Registry.register(Registry.BLOCK, new Identifier(name, "reproducer"), REPRODUCER);
        Registry.register(Registry.ITEM, new Identifier(name, "reproducer"), new BlockItem(REPRODUCER, new Item.Settings().group(HERBO_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(name, "machine_frame"), MACHINE_FRAME);
        Registry.register(Registry.ITEM, new Identifier(name, "machine_frame"), new BlockItem(MACHINE_FRAME, new Item.Settings().group(HERBO_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(name, "upgrader"), UPGRADER);
        Registry.register(Registry.ITEM, new Identifier(name, "upgrader"), new BlockItem(UPGRADER, new Item.Settings().group(HERBO_GROUP)));


        // Register blockentities
        STERILIZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "sterilizer"), BlockEntityType.Builder.create(SterilizerBlockEntity::new, STERILIZER).build(null));
        GROWTH_CONTROLLER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "growth_controller"), BlockEntityType.Builder.create(GrowthControllerBlockEntity::new, GROWTH_CONTROLLER).build(null));
        TURRET_SEED_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "turret_seed"), BlockEntityType.Builder.create(TurretSeedBlockEntity::new, TURRET_SEED_BLOCK).build(null));
        REPRODUCER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "reproducer"), BlockEntityType.Builder.create(ReproducerBlockEntity::new, REPRODUCER).build(null));
        UPGRADER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(name, "upgrader"), BlockEntityType.Builder.create(UpgraderBlockEntity::new, UPGRADER).build(null));

        // Register containers
        ScreenHandlerRegistry.registerExtended(STERILIZER_CONTAINER,
                (id, inv, buf) -> new SterilizerBlockContainer(id, buf.readText(), inv, buf.readBlockPos(), inv.player.world));
        ScreenHandlerRegistry.registerExtended(GROWTH_CONTROLLER_CONTAINER,
                (syncId, inv, buf) -> new GrowthControllerContainer(syncId, buf.readText(), inv, buf.readBlockPos(), inv.player.world));
        ScreenHandlerRegistry.registerExtended(REPRODUCER_CONTAINER,
                (syncId, inv, buf) -> new ReproducerBlockContainer(syncId, buf.readText(), inv, buf.readBlockPos(), inv.player.world));
        ScreenHandlerRegistry.registerExtended(UPGRADER_CONTAINER,
                (syncId, inv, buf) -> new UpgraderBlockContainer(syncId, buf.readText(), inv, buf.readBlockPos(), inv.player.world));
        // Register recipes
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(name, "patchouli_book"), new PatchouliBookRecipe.Serializer());

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
                        LOGGER.info("Loaded " + LOOTS.drops.size() + " turrets drops.");
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
