package com.redblueflame.herbocraft.entities;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.entities.ai.goal.TrackingGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TurretBaseEntity extends MobEntityWithAi implements RangedAttackMob {
    private static final TrackedData<Byte> TURRET_BASE_FLAGS;

    public TurretBaseEntity(EntityType<? extends MobEntityWithAi> entityType, World world) {
        super(entityType, world);
    }
    // 0.20000000298023224D
    // 0.00000000000000001D
    protected void initGoals() {
        this.goalSelector.add(1, new ProjectileAttackGoal(this, 0D, 10, 20.0F));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.targetSelector.add(1, new TrackingGoal<>(this, MobEntity.class, 10, true, false, (livingEntity) -> {
            return livingEntity instanceof Monster;
        }));
    }

    public static DefaultAttributeContainer.Builder createBaseTurretAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.00000000000000001D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2D);
    }


    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TURRET_BASE_FLAGS, (byte)16);
    }
    @Override
    public void attack(LivingEntity target, float pullProgress) {
        BulletEntity bulletEntity = BulletEntity.getWithOwner(this.world, this, 40f);
        double d = target.getEyeY() - 1.100000023841858D;
        double e = target.getX() - this.getX();
        double f = d - bulletEntity.getY();
        double g = target.getZ() - this.getZ();
        float h = MathHelper.sqrt(e * e + g * g) * 0.2F;
        bulletEntity.setVelocity(e, f + (double)h, g, 1.2F, 12.0F);
        this.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(bulletEntity);
    }
    static {
        TURRET_BASE_FLAGS = DataTracker.registerData(SnowGolemEntity.class, TrackedDataHandlerRegistry.BYTE);
    }
}