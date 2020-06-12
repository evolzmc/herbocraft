package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;


public class TurretSeed extends Item {
    public TurretSeed(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
        super.appendTooltip(stack, world, lines, ctx);
        LevelComponent comp = HerboCraft.LEVELLING.get(stack);
        lines.add(new TranslatableText("level_tooltips.health", comp.getHealth()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.attack_speed", comp.getAttackSpeed()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.damage", comp.getDamage()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.durability", getKeyFromQuality(getQualityFromDurability(comp.getDurability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.stability", getKeyFromQuality(getQualityFromStability(comp.getStability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
    private short getQualityFromStability(int stability) {
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
    private short getQualityFromDurability(int durability) {
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
    private Text getKeyFromQuality(short quality) {
        switch (quality) {
            case 0:
                return new TranslatableText("level_tooltips.stability.none").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true));
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