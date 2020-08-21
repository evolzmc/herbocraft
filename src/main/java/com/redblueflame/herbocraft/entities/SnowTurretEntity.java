package com.redblueflame.herbocraft.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class SnowTurretEntity extends TurretBaseEntity {
    public SnowTurretEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public BulletEntity getBulletEntity() {
        return BulletEntity.getWithOwner(this.world, this, super.getDamage(), new BulletEffect(StatusEffects.SLOWNESS, 3, 2));
    }
}
