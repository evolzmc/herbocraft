package com.redblueflame.herbocraft.blocks;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class UpgraderBlock extends AbstractUpgradableBlock implements BlockEntityProvider {
    protected UpgraderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return null;
    }
}
