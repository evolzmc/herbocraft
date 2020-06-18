package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
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

public class ReproducerBlockEntity extends BlockEntity implements Tickable {
    public short state;
    public boolean isWorking = false;
    public boolean isFinished = false;
    public int targetWork;
    public int currentWork;
    public Item currentItem1;
    public Item currentItem2;
    public BaseInventory inventory;

    public ReproducerBlockEntity() {
        super(HerboCraft.REPRODUCER_BLOCK_ENTITY);
    }

    /**
     * Gets the quality modifier, used for generation of variants.
     *
     * @return A modifier, where 1 is the lower.
     */
    private float getQualityMultiplier() {
        return 1F;
    }

    private QualityType getQualityType() {
        // TODO: Remove the hardcoded hell that is this code
        return QualityType.STANDARD;
    }

    @Override
    public void tick() {
        System.out.println("Testing !");
        if (isWorking) {
            currentWork += 1;
            state = (short) (((float) currentWork / (float) targetWork) * 255F);
            if (currentWork >= targetWork) {
                returnWork();
            }
        } else if (inventory != null) {
            // Check another time
            checkUpdate(inventory);
        }
    }

    public void registerEvents() {
        inventory.addListener(this::checkUpdate);
    }

    public void checkUpdate(Inventory inventory) {
        // Check if the slot 0 has changed
        ItemStack input1 = inventory.getStack(0);
        ItemStack input2 = inventory.getStack(1);
        boolean isSlotAvailable = this.getFirstAvailableSlot() != -1;
        if (!isSlotAvailable) {
            // The inventory is full, so don't work
            resetWork();
            return;
        }
        if (input1.getItem() == this.currentItem1
                && input2.getItem() == this.currentItem2
                && !isFinished) {
            // This seems to have not touched the input item, so no cancel.
            return;
        }
        // The slot has changed, so reset the current work.
        resetWork();
        this.currentItem1 = input1.getItem();
        this.currentItem2 = input2.getItem();
        if (!isAccepted(input1, input2)) {
            // This is an invalid item, we don't accept it
            return;
        }
        // Calculate the time objective
        targetWork = calculateWork(input1, input2);
        isWorking = true;
        isFinished = false;
    }

    public void returnWork() {
        ItemStack item = new ItemStack(currentItem1, 1);
        LevelComponent item1 = HerboCraft.LEVELLING.get(inventory.getStack(0));
        LevelComponent item2 = HerboCraft.LEVELLING.get(inventory.getStack(1));
        LevelComponent target = HerboCraft.LEVELLING.get(item);
        item1.reproduceWith(item2, target, new Random());
        // Add the turret
        inventory.setStack(getFirstAvailableSlot(), item);
        // Reset work
        resetWork();
        // Start next
        nextWork();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        // Save the current value of the number to the tag
        tag.putInt("currentWork", currentWork);
        tag.putInt("targetWork", targetWork);
        InventoryUtilities.write(inventory, tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        currentWork = tag.getInt("currentWork");
        targetWork = tag.getInt("targetWork");
        inventory = InventoryUtilities.read(tag);
    }

    //region Utility Functions
    private void resetWork() {
        isWorking = false;
        targetWork = 0;
        currentWork = 0;
        state = 0;
    }

    private void nextWork() {
        isFinished = true;
        checkUpdate(inventory);
    }

    private boolean isAccepted(ItemStack item1, ItemStack item2) {
        // We only check if the item is a valid choice
        return HerboCraft.SEEDS.contains(item1.getItem())
                && !HerboCraft.LEVELLING.get(item1).isSterile()
                && HerboCraft.SEEDS.contains(item2.getItem())
                && !HerboCraft.LEVELLING.get(item2).isSterile()
                && item1.getItem() == item2.getItem();
    }

    private int calculateWork(ItemStack input1, ItemStack input2) {
        LevelComponent item1 = HerboCraft.LEVELLING.get(input1);
        LevelComponent item2 = HerboCraft.LEVELLING.get(input2);
        return (int) ((600+(300-Math.min(item1.getStability(), item2.getStability()))*3)*getQualityMultiplier());
    }

    private int getFirstAvailableSlot() {
        // We skip the first slot to prevent errors.
        for (short i = 2; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    //endregion
}

