package com.redblueflame.herbocraft.blocks.cables;

import com.redblueflame.herbocraft.blocks.AbstractProgressBlock;
import com.redblueflame.herbocraft.transport.CableConnection;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class AbstractCableBlock extends Block implements BlockEntityProvider {
    public static final EnumProperty<CableConnection> NORTH = EnumProperty.of("north", CableConnection.class);
    public static final EnumProperty<CableConnection> EAST = EnumProperty.of("east", CableConnection.class);
    public static final EnumProperty<CableConnection> SOUTH = EnumProperty.of("south", CableConnection.class);
    public static final EnumProperty<CableConnection> WEST = EnumProperty.of("west", CableConnection.class);
    public static final EnumProperty<CableConnection> UP = EnumProperty.of("up", CableConnection.class);
    public static final EnumProperty<CableConnection> DOWN = EnumProperty.of("down", CableConnection.class);

    public AbstractCableBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState()
                .with(NORTH, CableConnection.DISCONNECTED)
                .with(EAST, CableConnection.DISCONNECTED)
                .with(SOUTH, CableConnection.DISCONNECTED)
                .with(WEST, CableConnection.DISCONNECTED)
                .with(UP, CableConnection.DISCONNECTED)
                .with(DOWN, CableConnection.DISCONNECTED)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        builder.add(UP);
        builder.add(DOWN);
    }

    private static Property<CableConnection> directionToProperty(Direction dir) {
        switch (dir) {
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case UP:
                return UP;
            case DOWN:
                return DOWN;
        }
        return null;
    }

    private static class CursedShapeHolder {
        private final VoxelShape connected;
        private final VoxelShape blockConnected;
        private final Property<CableConnection> prop;

        public CursedShapeHolder(Property<CableConnection> prop, VoxelShape connected, VoxelShape blockConnected1, VoxelShape blockConnected2) {
            this.connected = connected;
            this.blockConnected = VoxelShapes.combineAndSimplify(blockConnected1, blockConnected2, BooleanBiFunction.OR);
            this.prop = prop;
        }

        public VoxelShape get(BlockState state, VoxelShape shape) {
            switch (state.get(prop)) {
                case CONNECTED:
                    return VoxelShapes.union(shape, connected);
                case BLOCK_CONNECTED:
                    return VoxelShapes.union(shape, blockConnected);
            }
            return shape;
        }
    }

    private static final CursedShapeHolder NORTH_SHAPE = new CursedShapeHolder(NORTH,
            Block.createCuboidShape(4, 4, 0, 12, 12, 4),
            Block.createCuboidShape(4, 4, 1, 12, 12, 4),
            Block.createCuboidShape(3, 3, 0, 13, 13, 1));

    private static final CursedShapeHolder EAST_SHAPE = new CursedShapeHolder(EAST,
            Block.createCuboidShape(12, 4, 4, 16, 12, 12),
            Block.createCuboidShape(12, 4, 4, 15, 12, 12),
            Block.createCuboidShape(15, 3, 3, 16, 13, 13));

    private static final CursedShapeHolder SOUTH_SHAPE = new CursedShapeHolder(SOUTH,
            Block.createCuboidShape(4, 4, 12, 12, 12, 16),
            Block.createCuboidShape(4, 4, 12, 12, 12, 15),
            Block.createCuboidShape(3, 3, 15, 13, 13, 16));

    private static final CursedShapeHolder WEST_SHAPE = new CursedShapeHolder(WEST,
            Block.createCuboidShape(0, 4, 4, 4, 12, 12),
            Block.createCuboidShape(1, 4, 4, 4, 12, 12),
            Block.createCuboidShape(0, 3, 3, 1, 13, 13));

    private static final CursedShapeHolder UP_SHAPE = new CursedShapeHolder(UP,
            Block.createCuboidShape(4, 12, 4, 12, 16, 12),
            Block.createCuboidShape(4, 12, 4, 12, 15, 12),
            Block.createCuboidShape(3, 15, 3, 13, 16, 13));

    private static final CursedShapeHolder DOWN_SHAPE = new CursedShapeHolder(DOWN,
            Block.createCuboidShape(4, 0, 4, 12, 4, 12),
            Block.createCuboidShape(4, 1, 4, 12, 4, 12),
            Block.createCuboidShape(3, 0, 3, 13, 1, 13));

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        VoxelShape shape = Block.createCuboidShape(4, 4, 4, 12, 12, 12);
        shape = NORTH_SHAPE.get(state, shape);
        shape = EAST_SHAPE.get(state, shape);
        shape = SOUTH_SHAPE.get(state, shape);
        shape = WEST_SHAPE.get(state, shape);
        shape = UP_SHAPE.get(state, shape);
        shape = DOWN_SHAPE.get(state, shape);
        return shape;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = getDefaultState();
        // TODO: check fluid, check for component rather than side solid
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof AbstractCableBlock) {
                state = state.with(directionToProperty(dir), CableConnection.CONNECTED);
            } else if (neighborState.getBlock() instanceof AbstractProgressBlock) {
                state = state.with(directionToProperty(dir), CableConnection.BLOCK_CONNECTED);
            }
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // TODO: check fluid, check for component rather than side solid
        if (neighborState.getBlock() instanceof AbstractCableBlock) {
            return state.with(directionToProperty(facing), CableConnection.CONNECTED);
        } else if (neighborState.getBlock() instanceof AbstractProgressBlock) {
            return state.with(directionToProperty(facing), CableConnection.BLOCK_CONNECTED);
        }
        return state.with(directionToProperty(facing), CableConnection.DISCONNECTED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}
