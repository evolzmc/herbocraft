package com.redblueflame.herbocraft.entities;

import net.minecraft.entity.effect.StatusEffect;

public class BulletEffect {
    private final StatusEffect effect;
    private final int duration;
    private final int amplifier;

    public BulletEffect(StatusEffect effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration * 20;
        this.amplifier = amplifier;
    }

    public StatusEffect getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
