package com.redblueflame.herbocraft.components;

import nerdhub.cardinal.components.api.component.Component;

public interface LevelComponent extends Component {
    int getHealth();
    int getAttackSpeed();
    int getDamage();
    int getDurability();
    int getStability();
    boolean isSterile();
}
