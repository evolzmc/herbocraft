package com.redblueflame.herbocraft.components;

import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;


public interface LevelComponent extends Component, CopyableComponent<LevelComponent> {
    int getHealth();
    float getAttackSpeed();
    int getDamage();
    int getDurability();
    int getStability();
    boolean isSterile();
    void setSterile(boolean sterile);
    void resetLevels();
    void addLevels(int levels);
    void setStability(int stability);
}
