package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

public class GrowthControllerBlockEntity extends BlockEntity implements Tickable {
    public short state;
    public boolean isWorking;
    public boolean isFinished;
    public int targetWork;
    public int currentWork;
    public Item currentItem;
    public BaseInventory inventory;
    public GrowthControllerBlockEntity() {
        super(HerboCraft.GROWTH_CONTROLLER_BLOCK_ENTITY);
    }

    /**
     * Gets the quality modifier, used for generation of variants.
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
        if (isWorking) {
            currentWork += 1;
            state = (short) (((float)currentWork / (float)targetWork) * 255F);
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
        ItemStack input = inventory.getStack(0);
        boolean isSlotAvailable = this.getFirstAvailableSlot() != -1;
        if (!isSlotAvailable) {
            // The inventory is full, so don't work
            resetWork();
            return;
        }
        if (input.getItem() == this.currentItem && !isFinished) {
            // This seems to have not touched the input item, so no cancel.
            return;
        }
        // The slot has changed, so reset the current work.
        resetWork();
        this.currentItem = input.getItem();
        if (!isAccepted(input)) {
            // This is an invalid item, we don't accept it
            return;
        }
        // Calculate the time objective
        targetWork = calculateWork();
        isWorking = true;
        isFinished = false;
    }
    public void returnWork() {
        inventory.getStack(0).decrement(1);
        // Add the turret
        inventory.setStack(getFirstAvailableSlot(), HerboCraft.LOOTS.getRandomTurret(getQualityType()));
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
    private boolean isAccepted(ItemStack item) {
        // We only check if the item is a valid choice
        return HerboCraft.BASE_SEEDS.contains(item.getItem());
    }

    private int calculateWork() {
        return (int) (600*(1/getQualityMultiplier()));
    }

    private int getFirstAvailableSlot() {
        // We skip the first slot to prevent errors.
        for (short i = 1; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    //endregion
}
