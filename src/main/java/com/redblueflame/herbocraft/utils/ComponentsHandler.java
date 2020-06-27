package com.redblueflame.herbocraft.utils;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;

import java.util.Optional;

public class ComponentsHandler {
    public static LevelComponent getItemComponent(ItemStack stack) {
        return HerboCraft.LEVELLING.get(stack);
    }
    public static Optional<LevelComponent> getOptionalItemComponent(ItemStack stack) {
        return HerboCraft.LEVELLING.maybeGet(stack);
    }
}
