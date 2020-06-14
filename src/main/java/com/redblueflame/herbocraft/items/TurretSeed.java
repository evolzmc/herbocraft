package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.Converters;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
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

    @Override
    public boolean isIn(Tag<Item> tag) {
        return super.isIn(tag);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
        super.appendTooltip(stack, world, lines, ctx);
        LevelComponent comp = HerboCraft.LEVELLING.get(stack);
        lines.add(new TranslatableText("level_tooltips.health", comp.getHealth()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.attack_speed", comp.getAttackSpeed()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.damage", comp.getDamage()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.durability", Converters.getKeyFromQuality(Converters.getQualityFromDurability(comp.getDurability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.stability", Converters.getKeyFromQuality(Converters.getQualityFromStability(comp.getStability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if (comp.isSterile()) {
            lines.add(new TranslatableText("level_tooltips.sterile.true").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
        }
    }

}