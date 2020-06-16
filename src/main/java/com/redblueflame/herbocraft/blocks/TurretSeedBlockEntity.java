package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;


public class TurretSeedBlockEntity extends BlockEntity {
    private LevelComponent component;
    public TurretSeedBlockEntity() {
        super(HerboCraft.TURRET_SEED_BLOCK_ENTITY);
        component = new TurretLevelComponent();
    }
    public LevelComponent getComponent() {
        return component;
    }
    public void setComponent(LevelComponent component) {
        this.component = component;
    }
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        component.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        component.toTag(tag);
        return tag;
    }
}
