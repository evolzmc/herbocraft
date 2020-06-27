package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.utils.UpgradeSpawner;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GrowthController extends AbstractProgressBlock {
    public GrowthController(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new GrowthControllerBlockEntity();
    }
    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        super.onUse(state, world, pos, player, hand, hit);
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(HerboCraft.GROWTH_CONTROLLER_CONTAINER, player, (buffer) -> {
                buffer.writeText(new TranslatableText(this.getTranslationKey()));
                buffer.writeBlockPos(pos);
            });
        }
        return ActionResult.SUCCESS;
    }
    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            GrowthControllerBlockEntity blockEntity = (GrowthControllerBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null && blockEntity.inventory != null) {
                ItemScatterer.spawn(world, pos, blockEntity.inventory);
                world.updateComparators(pos, this);
            }
            ItemScatterer.spawn(world, pos, UpgradeSpawner.getItems(QualityType.fromInt(state.get(AbstractUpgradableBlock.UPGRADE_LEVEL))));
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


}
