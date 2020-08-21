package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.Converters;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.io.Console;
import java.util.Optional;
import java.util.logging.Level;

public class TurretAnalyzer extends Item {
    public int currentId = 0;
    public TurretAnalyzer(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof TurretBaseEntity)) {
            return ActionResult.FAIL;
        }
        TurretBaseEntity turretEntity = (TurretBaseEntity) entity;
        if (user.isSneaking() && turretEntity.getComponent() != null && currentId != entity.getEntityId()) {
            currentId = entity.getEntityId();
            LevelComponent comp = turretEntity.getComponent();
            user.sendMessage(new TranslatableText("level_tooltips.health", comp.getHealth()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
            user.sendMessage(new TranslatableText("level_tooltips.attack_speed", comp.getAttackSpeed()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
            user.sendMessage(new TranslatableText("level_tooltips.damage", comp.getDamage()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
            user.sendMessage(new TranslatableText("level_tooltips.durability", Converters.getKeyFromQuality(Converters.getQualityFromDurability(comp.getDurability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
            user.sendMessage(new TranslatableText("level_tooltips.stability", Converters.getKeyFromQuality(Converters.getQualityFromStability(comp.getStability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
        } else if (turretEntity.getComponent() == null) {
            HerboCraft.LOGGER.warn("The turret component is not initialized !");
        }
        return ActionResult.PASS;
    }

}
