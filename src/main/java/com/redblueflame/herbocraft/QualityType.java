package com.redblueflame.herbocraft;

public enum QualityType {
    STANDARD(0),

    TIER1(1),

    TIER2(2),

    TIER3(3),

    TIER4(4),
    ;

    private final int level;

    QualityType(int i) {
        level = i;
    }
    public int getLevel() {
        return level;
    }
    public boolean greaterOrEquals(QualityType other) {
        return level >= other.getLevel();
    }
}
