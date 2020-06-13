package com.redblueflame.herbocraft.components;

import com.redblueflame.herbocraft.HerboCraft;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class TurretLevelComponent implements LevelComponent, CopyableComponent<LevelComponent>
{
    private int health = 5;
    private float attackSpeed = 1;
    private int damage = 2;
    private int durability = 5;
    private int stability = 255;
    private boolean sterile = false;

    public static TurretLevelComponent getRandomStats(short level) {
        TurretLevelComponent comp = new TurretLevelComponent();
        comp.addRandomStats(level);
        return comp;
    }
    public void addRandomStats(short level) {
        Random rdm = new Random();
        for (int i = 0; i < level; i++) {
            switch (rdm.nextInt(4)) {
                case 0:
                    this.addLevelToStat("Health");
                    break;
                case 1:
                    this.addLevelToStat("AttackSpeed");
                    break;
                case 2:
                    this.addLevelToStat("Damage");
                    break;
                case 3:
                    this.addLevelToStat("Durability");
                    break;
            }
        }
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

    @Override
    public ComponentType<LevelComponent> getComponentType() {
        return HerboCraft.LEVELLING;
    }

    @Override
    public void copyFrom(LevelComponent other) {
        this.health = other.getHealth();
        this.durability = other.getDurability();
        this.damage = other.getDamage();
        this.attackSpeed = other.getAttackSpeed();
        this.sterile = other.isSterile();
        this.stability = other.getStability();
    }
}