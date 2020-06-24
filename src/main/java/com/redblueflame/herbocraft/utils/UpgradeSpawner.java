package com.redblueflame.herbocraft.utils;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;


import java.util.ArrayList;
import java.util.List;

public class UpgradeSpawner {
    public static DefaultedList<ItemStack> getItems(QualityType currentLevel) {
        if (currentLevel.getLevel() == 0) {
            return DefaultedList.of();
        }
        ArrayList<ItemStack> items = new ArrayList<>();

        for (int i = 0; i <= currentLevel.getLevel(); i++) {
            Item item = getItem(QualityType.fromInt(i));
            if (item != null) {
                items.add(new ItemStack(item, 1));
            }
        }
        return DefaultedList.copyOf(null, items.toArray(new ItemStack[]{}));
    }

    private static Item getItem(QualityType type) {
        switch (type) {
            case TIER1:
                return HerboCraft.TIER1_UPGRADE;
            case TIER2:
                return HerboCraft.TIER2_UPGRADE;
            case TIER3:
                return HerboCraft.TIER3_UPGRADE;
            case TIER4:
                return HerboCraft.TIER4_UPGRADE;
            default:
                return null;
        }
    }
}
