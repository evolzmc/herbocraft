package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

import java.util.Optional;

public class UpgraderBlockEntity extends BlockEntity implements Tickable {
    public int state;
    public boolean isWorking;
    public BaseInventory inventory;
    public ItemStack currentItem;

    private int currentWorkProgress;
    private int targetWorkProgress;

    public UpgraderBlockEntity() {
        super(HerboCraft.UPGRADER_BLOCK_ENTITY);
        state = 0;
        isWorking = false;
        currentWorkProgress = 0;
        targetWorkProgress = 0;
    }

    @Override
    public void tick() {
        if (isWorking) {
            currentWorkProgress += 1;
            state = (int) (((float)currentWorkProgress / targetWorkProgress) * 255);
            if (currentWorkProgress >= targetWorkProgress) {
                workFinished();
            }
        }
        if (inventory == null) {
            return;
        }
        // Check if work is available
        checkWork();
    }

    // region Work related functions

    /**
     * This function should be as short as possible, as it is called every single tick.
     */
    private void checkWork() {
        if (inventory.getStack(10).isEmpty()) {
            resetWork();
            // Check of the input slots
            int input = getFirstInputSlot();
            if (input == -1) {
                return;
            }
            // Put the item in the slot 10
            inventory.setStack(10, inventory.getStack(input));
            inventory.removeStack(input);
        }
        ItemStack itemStack = inventory.getStack(10);
        // Check if the item has the necessary properties
        Optional<LevelComponent> comp = HerboCraft.LEVELLING.maybeGet(itemStack);
        if (!comp.isPresent()) {
            // The component is not present, we do nothing.
            resetWork();
            return;
        }
        int availableSlot = getFirstAvailableSlot();
        if (availableSlot == -1) {
            // The output is full, we stop.
            resetWork();
            return;
        }
        // Check if the plant has not reached the limit of upgrades or is sterile
        if (comp.get().isSterile() || comp.get().getStability() <= 25) {
            return;
        }
        if (isWorking) {
            // Everything is valid, and there is still work, so we wait !
            return;
        }
        // We can start the work
        startWork();
    }
    private void resetWork() {
        currentWorkProgress = 0;
        targetWorkProgress = 0;
        isWorking = false;
        state = 0;
    }
    private void startWork() {
        targetWorkProgress = calculateWork();
        currentWorkProgress = 0;
        state = 0;
        isWorking = true;
    }
    private int calculateWork() {
        return (int) (600*getQualityMultiplier());
    }
    private void onInventoryUpdate() {
        // Check if the middle slot is empty
        if (inventory.getStack(10).isEmpty()) {
            int itemSlot = getFirstInputSlot();
            if (itemSlot != -1) {
                // Switch inventory
                inventory.setStack(10, inventory.getStack(itemSlot));
                inventory.removeStack(itemSlot);
                // Check the work
                checkWork();
            }
        }

    }
    private void workFinished() {
        if (inventory.getStack(10).isEmpty()) {
            resetWork();
            return;
        }
        ItemStack itemStack = inventory.getStack(10);
        // Check if the item has the necessary properties
        Optional<LevelComponent> comp = HerboCraft.LEVELLING.maybeGet(itemStack);
        if (!comp.isPresent()) {
            // The component is not present, we do nothing.
            resetWork();
            return;
        }
        comp.get().addLevels(1);
        comp.get().setStability(comp.get().getStability()-25);
        // Great !
        if (comp.get().getStability() <= 25) {
            // Move the item to an output spot
            int target = getFirstAvailableSlot();
            if (target == -1) {
                resetWork();
            }
            inventory.setStack(target, inventory.getStack(10));
            inventory.removeStack(10);
        }
        resetWork();
        checkWork();
    }

    private float getQualityMultiplier() {
        return QualityType.fromInt(world.getBlockState(pos).get(AbstractUpgradableBlock.UPGRADE_LEVEL)).getMultiplier();
    }

    private int getFirstInputSlot() {
        for (int i = 0; i < 10; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    private int getFirstAvailableSlot() {
        for (int i = 11; i < 20; i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    // endregion
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        // Save the current value of the number to the tag
        tag.putInt("currentWork", currentWorkProgress);
        tag.putInt("targetWork", targetWorkProgress);
        tag.putBoolean("isWorking", isWorking);
        InventoryUtilities.write(inventory, tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        currentWorkProgress = tag.getInt("currentWork");
        targetWorkProgress = tag.getInt("targetWork");
        isWorking = tag.getBoolean("isWorking");
        inventory = InventoryUtilities.read(tag);
    }
}
