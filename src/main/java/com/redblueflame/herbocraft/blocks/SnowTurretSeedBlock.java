package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Optional;

public class SnowTurretSeedBlock extends TurretSeedBlock{
    public SnowTurretSeedBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Entity getEntity(World world, LevelComponent itemComp) {
        SnowTurretEntity entity = new SnowTurretEntity(HerboCraft.SNOW_TURRET, world);
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
}
