package com.redblueflame.herbocraft.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import static com.redblueflame.herbocraft.HerboCraft.BULLET;

public class BulletEntity extends ThrownItemEntity {
    private float damage;
    private BulletEffect bulletEffect;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
        this.damage = 2f;
        this.bulletEffect = null;
    }

    private BulletEntity(EntityType<? extends BulletEntity> entityType, World world, LivingEntity owner, float damage) {
        super(entityType, owner, world);
        this.damage = damage;
    }

    private BulletEntity(EntityType<? extends BulletEntity> entityType, World world, LivingEntity owner, BulletEffect bulletEffect) {
        super(entityType, owner, world);
        this.damage = 2f;
        this.bulletEffect = bulletEffect;
    }

    private BulletEntity(EntityType<? extends BulletEntity> entityType, World world, LivingEntity owner, float damage, BulletEffect bulletEffect) {
        super(entityType, owner, world);
        this.damage = damage;
        this.bulletEffect = bulletEffect;
    }

    public static BulletEntity getWithOwner(World world, LivingEntity owner, float damage, BulletEffect bulletEffect) {
        return new BulletEntity(BULLET, world, owner, damage, bulletEffect);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setBulletEffect(BulletEffect bulletEffect) {
        this.bulletEffect = bulletEffect;
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), damage);
        if (entity instanceof LivingEntity && this.bulletEffect != null) ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(this.bulletEffect.getEffect(), this.bulletEffect.getDuration(), this.bulletEffect.getAmplifier()));
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return super.createSpawnPacket();
    }
}
