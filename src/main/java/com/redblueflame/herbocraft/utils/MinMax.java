package com.redblueflame.herbocraft.utils;

import java.util.Random;

public class MinMax {
    public int min;
    public int max;
    public MinMax() {

    }
    public int getRandom(Random rdm) {
        return rdm.nextInt(max-min) + min;
    }
}
