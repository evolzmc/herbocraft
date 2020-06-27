package com.redblueflame.herbocraft.blocks;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import spinnery.common.inventory.BaseInventory;

public interface Progress {
    boolean checkWork();
    void finishWork();
    int estimateWork();
    void resetWork();
    BaseInventory getInventory();
    /**
     * This function is used for the user to check if the input slot has been updated.
     * @return True if the input slot has been touched.
     */
    boolean inventoryUpdate(Inventory inventory);
    Class<?> getContainerClass();
}
