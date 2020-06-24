package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.blocks.AbstractUpgradableBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class UpgradeItem extends Item {
    QualityType type;
    float multiplier;

    public UpgradeItem(Settings settings, QualityType type) {
        super(settings);
        this.type = type;
        this.multiplier = type.getMultiplier();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).isIn(HerboCraft.UPGRADABLE_BLOCKS)) {
            if (type.getLevel() <= context.getWorld().getBlockState(context.getBlockPos()).get(AbstractUpgradableBlock.UPGRADE_LEVEL)) {
                // The level is too low or too high to apply the upgrade, we do nothing.
                return ActionResult.FAIL;
            }
            if (type.getLevel() != context.getWorld().getBlockState(context.getBlockPos()).get(AbstractUpgradableBlock.UPGRADE_LEVEL)+1) {
                Objects.requireNonNull(context.getPlayer()).sendMessage(new TranslatableText("upgrade.skipped_level").setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
                return ActionResult.FAIL;
            }
            // The block is an upgradable one !
            context.getWorld().setBlockState(
                    context.getBlockPos(),
                    context.getWorld().getBlockState(context.getBlockPos()).with(AbstractUpgradableBlock.UPGRADE_LEVEL, this.type.getLevel()));
            context.getWorld().playSound(null,
                    context.getBlockPos(),
                    SoundEvents.BLOCK_ANVIL_USE,
                    SoundCategory.BLOCKS,
                    1f,
                    1f);
            context.getStack().decrement(1);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
