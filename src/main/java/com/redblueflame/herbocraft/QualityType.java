package com.redblueflame.herbocraft;

import net.minecraft.item.Item;
import net.minecraft.util.ItemScatterer;

public enum QualityType {
    STANDARD(0, 1F),

    TIER1(1, 0.9F),

    TIER2(2, 0.8F),

    TIER3(3,0.6F),

    TIER4(4, 0.4F),
    ;

    private final int level;
    private final float multiplier;

    QualityType(int i, float j) {
        level = i;
        multiplier = j;
    }
    public int getLevel() {
        return level;
    }
    public float getMultiplier() {
        return multiplier;
    }
    public static QualityType fromInt(int i) {
        switch (i) {
            case 0:
                return STANDARD;
            case 1:
                return TIER1;
            case 2:
                return TIER2;
            case 3:
                return TIER3;
            case 4:
                return TIER4;
            default:
                throw new RuntimeException("The value asked it not available in QualityType enum");
        }
    }
    public boolean greaterOrEquals(QualityType other) {
        return level >= other.getLevel();
    }
    public static int getLevelCount() { return 5; }
}
