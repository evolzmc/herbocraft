package com.redblueflame.herbocraft.components;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class TurretLevelComponent implements LevelComponent
{
    private int health = 5;
    private float attackSpeed = 1;
    private int damage = 2;
    private int durability = 5;
    private int stability = 255;
    private boolean sterile = false;

    public static TurretLevelComponent getRandomStats(short level) {
        TurretLevelComponent comp = new TurretLevelComponent();
        Random rdm = new Random();
        for (int i = 0; i < level; i++) {
            switch (rdm.nextInt(4)) {
                case 0:
                    comp.addLevelToStat("Health");
                    break;
                case 1:
                    comp.addLevelToStat("AttackSpeed");
                    break;
                case 2:
                    comp.addLevelToStat("Damage");
                    break;
                case 3:
                    comp.addLevelToStat("Durability");
                    break;
            }
        }
        return comp;
    }

    private void addLevelToStat(String name) {
        switch (name) {
            case "Health":
                health += 4;
                break;
            case "AttackSpeed":
                attackSpeed += 0.2;
                break;
            case "Damage":
                damage += 1;
                break;
            case "Durability":
                durability+=15;
                break;
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getAttackSpeed() {
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
        tag.putFloat("AttackSpeed", this.attackSpeed);
        tag.putInt("Damage", this.damage);
        tag.putInt("Durability", this.durability);
        tag.putInt("Stability", this.stability);
        tag.putBoolean("Sterile", this.sterile);
        return tag;
    }
}