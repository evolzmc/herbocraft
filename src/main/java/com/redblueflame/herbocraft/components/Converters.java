package com.redblueflame.herbocraft.components;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class Converters {
    public static short getQualityFromStability(int stability) {
        if (stability >= 250) {
            return 5;
        }
        if (stability >= 200) {
            return 4;
        }
        if (stability >= 150) {
            return 3;
        }
        if (stability >= 100) {
            return 2;
        }
        if (stability >= 25) {
            return 1;
        }
        return 0;
    }
    public static short getQualityFromDurability(int durability) {
        if (durability >= 100) {
            return 5;
        }
        if (durability >= 90) {
            return 4;
        }
        if (durability >= 75) {
            return 3;
        }
        if (durability >= 50) {
            return 2;
        }
        if (durability >= 25) {
            return 1;
        }
        return 0;
    }
    public static Text getKeyFromQuality(short quality) {
        switch (quality) {
            case 0:
                return new TranslatableText("level_tooltips.stability.none").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
            case 1:
                return new TranslatableText("level_tooltips.stability.ultra-low").setStyle(Style.EMPTY.withColor(Formatting.RED));
            case 2:
                return new TranslatableText("level_tooltips.stability.low").setStyle(Style.EMPTY.withColor(Formatting.GOLD));
            case 3:
                return new TranslatableText("level_tooltips.stability.med").setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            case 4:
                return new TranslatableText("level_tooltips.stability.high").setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            case 5:
                return new TranslatableText("level_tooltips.stability.extreme").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN));
            default:
                return new TranslatableText("level_tooltips.stability.unknown").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
        }
    }
}
