package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

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
        int availableSlot = getFirstAvailableSlot();
        if (availableSlot == -1) {
            // The output is full, we stop.
            resetWork();
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
        for (int i = 10; i < 20; i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    // endregion
}
