package com.redblueflame.herbocraft.components;

import net.minecraft.nbt.CompoundTag;

public class TurretLevelComponent implements LevelComponent
{
    private int health = 5;
    private int attackSpeed = 1;
    private int damage = 2;
    private int durability = 5;
    private int stability = 255;
    private boolean sterile = false;

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public int getStability() {
        return stability;
    }

    @Override
    public boolean isSterile() {
        return sterile;
    }


    @Override
    public void fromTag(CompoundTag tag) {
        this.health = tag.getInt("Health");
        this.attackSpeed = tag.getInt("AttackSpeed");
        this.damage = tag.getInt("Damage");
        this.durability = tag.getInt("Durability");
        this.stability = tag.getInt("Stability");
        this.sterile = tag.getBoolean("Sterile");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("Health", this.health);
        tag.putInt("AttackSpeed", this.attackSpeed);
        tag.putInt("Damage", this.damage);
        tag.putInt("Durability", this.durability);
        tag.putInt("Stability", this.stability);
        tag.putBoolean("Sterile", this.sterile);
        return tag;
    }
}
