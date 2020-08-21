package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.WitherTurretEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Optional;

public class WitherTurretSeedBlock extends TurretSeedBlock{
    public WitherTurretSeedBlock(Settings settings) {
        super(settings);
    }
    @Override
    public Entity getEntity(World world, LevelComponent itemComp) {
        WitherTurretEntity entity = new WitherTurretEntity(HerboCraft.WITHER_TURRET, world);
        entity.setComponent(itemComp);
        entity.setAttributes();
        return entity;
    }
}
