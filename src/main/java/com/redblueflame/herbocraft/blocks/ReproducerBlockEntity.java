package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.utils.ComponentsHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

import java.util.Random;

public class ReproducerBlockEntity extends AbstractProgressBlockEntity {
    public ItemStack currentItem1;
    public ItemStack currentItem2;
    public BaseInventory inventory;

    public ReproducerBlockEntity() {
        super(HerboCraft.REPRODUCER_BLOCK_ENTITY);
    }

    @Override
    public boolean checkWork() {
        ItemStack input1 = inventory.getStack(0);
        ItemStack input2 = inventory.getStack(1);
        boolean isSlotAvailable = this.getFirstAvailableSlot() != -1;
        if (!isSlotAvailable) {
            return false;
        }
        if (!isAccepted(input1, input2)) {
            return false;
        }
        currentItem1 = input1;
        currentItem2 = input2;
        return true;
    }

    @Override
    public void finishWork() {
        ItemStack item = new ItemStack(currentItem1.getItem(), 1);
        LevelComponent item1 = ComponentsHandler.getItemComponent(inventory.getStack(0), true);
        LevelComponent item2 = ComponentsHandler.getItemComponent(inventory.getStack(1),  true);
        LevelComponent target = ComponentsHandler.createItemComponent(item);
        item1.reproduceWith(item2, target, new Random());
        int slot = getFirstAvailableSlot();
        if (slot == -1) {
            return;
        }
        inventory.setStack(slot, item);
    }

    @Override
    public int estimateWork() {
        LevelComponent item1 = ComponentsHandler.getItemComponent(inventory.getStack(0));
        LevelComponent item2 = ComponentsHandler.getItemComponent(inventory.getStack(1));
        return (int) ((600+(300-Math.min(item1.getStability(), item2.getStability()))*3)*getQualityMultiplier());
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean inventoryUpdate(Inventory inventory) {
        ItemStack input1 = inventory.getStack(0);
        ItemStack input2 = inventory.getStack(1);
        return !(input1 == this.currentItem1
                && input2 == this.currentItem2);
    }

    @Override
    public Class<?> getContainerClass() {
        return ReproducerBlockContainer.class;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        InventoryUtilities.write(inventory, tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        inventory = InventoryUtilities.read(tag);
        if (inventory != null) {
            currentItem1 = inventory.getStack(0);
            currentItem2 = inventory.getStack(1);
        }
    }

    // region Quality types

    private float getQualityMultiplier() {
        return QualityType.fromInt(world.getBlockState(pos).get(AbstractUpgradableBlock.UPGRADE_LEVEL)).getMultiplier();
    }


    //endregion

    // region Utils
    private int getFirstAvailableSlot() {
        // We skip the first slot to prevent errors.
        for (short i = 2; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isAccepted(ItemStack item1, ItemStack item2) {
        // We only check if the item is a valid choice
        return HerboCraft.SEEDS.contains(item1.getItem())
                && !ComponentsHandler.getItemComponent(inventory.getStack(0)).isSterile()
                && HerboCraft.SEEDS.contains(item2.getItem())
                && !ComponentsHandler.getItemComponent(inventory.getStack(0)).isSterile()
                && item1.getItem() == item2.getItem();
    }
    // endregion
}

