package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.QualityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;

public abstract class AbstractUpgradableBlock extends BlockWithEntity {
    public static final IntProperty UPGRADE_LEVEL = IntProperty.of("upgrade", 0, QualityType.getLevelCount());
    protected AbstractUpgradableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(UPGRADE_LEVEL, 0));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UPGRADE_LEVEL);
    }
}
