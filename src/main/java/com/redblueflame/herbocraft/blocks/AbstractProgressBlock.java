package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public abstract class AbstractProgressBlock extends AbstractUpgradableBlock implements BlockEntityProvider {
    protected AbstractProgressBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            // Sync the server with client for the state of the block.
            AbstractProgressBlockEntity blockEntity = (AbstractProgressBlockEntity) world.getBlockEntity(pos);
            if (blockEntity == null) {
                HerboCraft.LOGGER.error("There were an error while getting the blockentity. Please check your input.");
                return ActionResult.PASS;
            }
            blockEntity.sendBlockProgressPacket(player);
        }
        return ActionResult.PASS;
    }
}
