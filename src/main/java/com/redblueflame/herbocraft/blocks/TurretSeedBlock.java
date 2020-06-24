package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class TurretSeedBlock extends CropBlock implements BlockEntityProvider {
    public static final IntProperty AGE = Properties.AGE_5;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
    };

    public TurretSeedBlock(Settings settings) {
        super(settings);
    }

    public Entity getEntity(World world, LevelComponent itemComp) {
        TurretBaseEntity entity = new TurretBaseEntity(HerboCraft.TURRET_BASE, world);
        // Set the Level Component
        Optional<LevelComponent> opt_comp = HerboCraft.LEVELLING.maybeGet(entity);
        if (!opt_comp.isPresent()) {
            throw new RuntimeException("Seems like the entity isn't compatible with the custom tags. Please check your entry.");
        }
        LevelComponent entityComp = opt_comp.get();
        entityComp.copyFrom(itemComp);
        entity.setAttributes(entityComp);
        return entity;
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 4;
    }

    @Environment(EnvType.CLIENT)
    protected ItemConvertible getSeedsItem() {
        return Items.BEETROOT_SEEDS;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(3) != 0) {
            super.randomTick(state, world, pos, random);
        }
        checkGrowth(state, world, pos);
    }

    private void checkGrowth(BlockState state, ServerWorld world, BlockPos pos) {
        if (getAge(state) >= getMaxAge() - 1) {
            // The plant is at max growth, we delete the block, and summon the turret
            TurretSeedBlockEntity blockEntity = (TurretSeedBlockEntity) world.getBlockEntity(pos);
            if (blockEntity == null) {
                throw new RuntimeException("The block entity was not created.");
            }
            Entity entity = getEntity(world, blockEntity.getComponent());
            entity.teleport(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
            world.spawnEntity(entity);
            world.breakBlock(pos, false);
        }
    }

    protected int getGrowthAmount(World world) {
        return super.getGrowthAmount(world) / 4;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        super.grow(world, random, pos, state);
        System.out.println("GROW ! " + getAge(state) + "/" + getMaxAge());
        checkGrowth(state, world, pos);
    }


    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TurretSeedBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}
