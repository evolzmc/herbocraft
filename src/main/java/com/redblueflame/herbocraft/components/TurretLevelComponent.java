package com.redblueflame.herbocraft.components;

import com.redblueflame.herbocraft.HerboCraft;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class TurretLevelComponent implements LevelComponent {
    private int health = 5;
    private float attackSpeed = 1;
    private int damage = 2;
    private int durability = 200;
    private int stability = 255;
    private boolean dirty = true;
    private boolean sterile = false;

    public static TurretLevelComponent getRandomStats(short level) {
        TurretLevelComponent comp = new TurretLevelComponent();
        comp.addLevels(level);
        return comp;
    }

    @Override
    public void resetLevels() {
        health = 5;
        attackSpeed = 1;
        damage = 2;
        durability = 200;
        stability = 255;
        sterile = false;
    }

    @Override
    public void addLevels(int levels) {
        addRandomStats(levels);
    }

    private void addRandomStats(int level) {
        Random rdm = new Random();
        for (int i = 0; i < level; i++) {
            switch (rdm.nextInt(7)) {
                case 0:
                case 1:
                    this.addLevelToStat("Health");
                    break;
                case 2:
                case 3:
                    this.addLevelToStat("AttackSpeed");
                    break;
                case 4:
                case 5:
                    this.addLevelToStat("Damage");
                    break;
                case 6:
                    // Made this less probable to prevent infinite replication.
                    this.addLevelToStat("Durability");
                    break;
            }
        }
    }

    private void addLevelToStat(String name) {
        switch (name) {
            case "Health":
                health += 1;
                break;
            case "AttackSpeed":
                attackSpeed += 0.1;
                break;
            case "Damage":
                damage += 0.5;
                break;
            case "Durability":
                durability += 1;
                break;
        }
        dirty = true;
    }

    @Override
    public void reproduceWith(LevelComponent other, LevelComponent target, Random rdm) {
        int sep = Math.abs(this.health - other.getHealth());
        target.setHealth(Math.min(this.health, other.getHealth()) + rdm.nextInt(sep+1));
        float sep_attack_speed = Math.abs(this.attackSpeed - other.getAttackSpeed());
        target.setAttackSpeed(Math.min(this.attackSpeed, other.getAttackSpeed()) + rdm.nextFloat()*(sep_attack_speed));
        sep = Math.abs(this.damage - other.getDamage());
        target.setDamage(Math.min(this.damage, other.getDamage()) + rdm.nextInt(sep+1));
        target.setDurability(Math.min(this.durability, other.getDurability()) + rdm.nextInt(50));
        target.setStability(200 + rdm.nextInt(55));
        dirty = true;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
        dirty = true;
    }

    @Override
    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
        dirty = true;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
        dirty = true;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
        dirty = true;
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
    public void setSterile(boolean sterile) {
        this.sterile = sterile;
        dirty = true;
    }

    @Override
    public void setStability(int stability) {
        this.stability = stability;
        dirty = true;
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

    public void copyFrom(LevelComponent other) {
        this.health = other.getHealth();
        this.durability = other.getDurability();
        this.damage = other.getDamage();
        this.attackSpeed = other.getAttackSpeed();
        this.sterile = other.isSterile();
        this.stability = other.getStability();
    }

    @Override
    public String toString() {
        return "TurretLevelComponent{" +
                "health=" + health +
                ", attackSpeed=" + attackSpeed +
                ", damage=" + damage +
                ", durability=" + durability +
                ", stability=" + stability +
                ", dirty=" + dirty +
                ", sterile=" + sterile +
                '}';
    }

    public boolean isDirty() {
        return dirty;
    }
    public void clearDirty() {
        dirty = false;
    }
}