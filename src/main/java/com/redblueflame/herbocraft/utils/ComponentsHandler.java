package com.redblueflame.herbocraft.utils;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.blocks.TurretSeedBlockEntity;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.items.TurretSeed;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class ComponentsHandler {

    public static LevelComponent getBlockEntityComponent(BlockEntity entity) {
        return getBlockEntityComponent(entity, true);
    }

    public static LevelComponent getBlockEntityComponent(BlockEntity entity, boolean create) {
        if (!(entity instanceof TurretSeedBlockEntity)) {
            // The blockentity is not supported
            HerboCraft.LOGGER.error("The blockentity is not supported.");
            return null;
        }
        LevelComponent comp = ((TurretSeedBlockEntity) entity).getComponent();
        if (comp == null && create) {
            comp = TurretLevelComponent.getRandomStats((short) 5);
            ((TurretSeedBlockEntity) entity).setComponent(comp);
        }
        return comp;
    }

    public static Optional<LevelComponent> getOptionalBlockEntityComponent(BlockEntity entity) {
        LevelComponent comp = getBlockEntityComponent(entity, false);
        if (comp == null) {
            return Optional.empty();
        }
        return Optional.of(comp);
    }


    public static void setBlockEntityComponent(BlockEntity entity, LevelComponent component) {
        if (!(entity instanceof TurretSeedBlockEntity)) {
            // The blockentity is not supported
            HerboCraft.LOGGER.error("The blockentity is not supported.");
            return;
        }
        ((TurretSeedBlockEntity) entity).setComponent(component);
    }

    public static LevelComponent getItemComponent(ItemStack stack) {
        return getItemComponent(stack, false);
    }
    public static LevelComponent getItemComponent(ItemStack stack, boolean create) {
        if (!(stack.getItem() instanceof TurretSeed)) {
            return null;
        }
        CompoundTag levelTag;
        if (create) {
            levelTag = stack.getSubTag("LevelComponent");
            if (levelTag == null) {
                levelTag = new CompoundTag();
                levelTag = createComponentTag(levelTag);
                stack.putSubTag("LevelComponent", levelTag);
            }
        } else {
            levelTag = stack.getSubTag("LevelComponent");
        }
        if (levelTag == null) {
            return null;
        }
        TurretLevelComponent component = new TurretLevelComponent();
        component.fromTag(levelTag);
        return component;
    }

    public static LevelComponent createItemComponent(ItemStack stack) {
        TurretLevelComponent comp = TurretLevelComponent.getRandomStats((short) 5);
        CompoundTag tag = stack.getOrCreateSubTag("LevelComponent");
        tag = comp.toTag(tag);
        stack.setTag(tag);
        return comp;
    }
    public static CompoundTag createComponentTag(CompoundTag tag) {
        TurretLevelComponent comp = TurretLevelComponent.getRandomStats((short) 5);
        return comp.toTag(tag);
    }

    public static void saveItemComponent(ItemStack stack, LevelComponent component) {
        CompoundTag levelTag = new CompoundTag();
        stack.putSubTag("LevelComponent", component.toTag(levelTag));
    }

    public static Optional<LevelComponent> getOptionalItemComponent(ItemStack stack) {
        LevelComponent component = getItemComponent(stack);
        if (component == null) {
            return Optional.empty();
        }
        return Optional.of(component);
    }
}
