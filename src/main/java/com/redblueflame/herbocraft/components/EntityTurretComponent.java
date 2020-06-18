package com.redblueflame.herbocraft.components;

import net.minecraft.entity.LivingEntity;

public class EntityTurretComponent extends TurretLevelComponent {
    protected LivingEntity owner;

    public EntityTurretComponent(LivingEntity owner, short level) {
        this.owner = owner;
        this.addLevels(level);
    }

}
