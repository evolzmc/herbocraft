package com.redblueflame.herbocraft.entities;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.EntityTurretComponent;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.entities.ai.goal.TrackingGoal;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TurretBaseEntity extends PathAwareEntity implements RangedAttackMob {
    private static final TrackedData<Byte> TURRET_BASE_FLAGS;
    private Goal projectileGoal;
    private LevelComponent component;
    public float damage = 40F;

    static {
        TURRET_BASE_FLAGS = DataTracker.registerData(TurretBaseEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    public TurretBaseEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        projectileGoal = new ProjectileAttackGoal(this, 0D, 10, 40F);
        component = new TurretLevelComponent();
    }

    public static DefaultAttributeContainer.Builder createBaseTurretAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.00000000000000001D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2D);
    }

    public void setAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(component.getHealth());
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(component.getAttackSpeed());
        this.goalSelector.remove(projectileGoal);
        projectileGoal = new ProjectileAttackGoal(this, 0D, (int) (20*component.getAttackSpeed()), 40F);
        this.goalSelector.add(1, projectileGoal);
        damage = component.getDamage();

    }


    // 0.20000000298023224D
    // 0.00000000000000001D
    protected void initGoals() {
        if (projectileGoal == null) {
            projectileGoal = new ProjectileAttackGoal(this, 0D, 10, 40F);
        }
        this.goalSelector.add(1, projectileGoal);
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.targetSelector.add(1, new TrackingGoal<>(this, MobEntity.class, 10, true, false, (livingEntity) -> livingEntity instanceof Monster));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TURRET_BASE_FLAGS, (byte) 16);
    }
    @Override
    public void attack(LivingEntity target, float pullProgress) {
        BulletEntity bulletEntity = getBulletEntity();
        double d = target.getEyeY() - 1.100000023841858D;
        double e = target.getX() - this.getX();
        double f = d - bulletEntity.getY();
        double g = target.getZ() - this.getZ();
        float h = MathHelper.sqrt(e * e + g * g) * 0.2F;
        bulletEntity.setVelocity(e, f + (double) h, g, 1.2F, 2.0F);
        this.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(bulletEntity);
    }

    public BulletEntity getBulletEntity() {
        return BulletEntity.getWithOwner(this.world, this, this.damage, null);
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public boolean canMoveVoluntarily() {
        return true;
    }

    @Override
    protected void pushAway(Entity entity) {
        // Do nothing at all
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        // Do nothing, this shouldn't move
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        // Do nothing.
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void takeKnockback(float f, double d, double e) {
        // Do nothing, this shouldn't get any knockback
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        // Extract the component
        LevelComponent comp = new TurretLevelComponent();
        comp.fromTag(tag.getCompound("LevelComponent"));
        component = comp;
        setAttributes();
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        CompoundTag subTag = tag.getCompound("LevelComponent");
        tag.put("LevelComponent", component.toTag(subTag));
    }

    public LevelComponent getComponent() {
        return component;
    }

    public void setComponent(LevelComponent component) {
        this.component = component;
    }
}
