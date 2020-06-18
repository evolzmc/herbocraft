package com.redblueflame.herbocraft.components;

import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;

import java.util.Random;


public interface LevelComponent extends Component, CopyableComponent<LevelComponent> {
    int getHealth();
    void setHealth(int health);
    float getAttackSpeed();
    void setAttackSpeed(float attackSpeed);
    int getDamage();
    void setDamage(int damage);
    int getDurability();
    void setDurability(int durability);
    int getStability();
    void setStability(int stability);
    boolean isSterile();
    void setSterile(boolean sterile);
    void resetLevels();
    void addLevels(int levels);
    void reproduceWith(LevelComponent other, LevelComponent target, Random random);
}
