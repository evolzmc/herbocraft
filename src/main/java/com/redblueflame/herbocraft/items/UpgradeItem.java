package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.QualityType;
import net.minecraft.item.Item;

public class UpgradeItem extends Item {
    QualityType type;
    float multiplier;

    public UpgradeItem(Settings settings, QualityType type, float multiplier) {
        super(settings);
        this.type = type;
        this.multiplier = multiplier;

    }
}
