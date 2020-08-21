package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

public class GrowthControllerBlockEntity extends AbstractProgressBlockEntity {
    public Item currentItem;
    public BaseInventory inventory;

    public GrowthControllerBlockEntity() {
        super(HerboCraft.GROWTH_CONTROLLER_BLOCK_ENTITY);
    }

    @Override
    public boolean checkWork() {
        // Get the seed slot
        ItemStack input = inventory.getStack(0);
        boolean isSlotAvailable = this.getFirstAvailableSlot() != -1;
        if (!isSlotAvailable) {
            // The inventory is full, so don't work
            return false;
        }
        if (!isAccepted(input)) {
            // This is an invalid item, we don't accept it
            return false;
        }
        currentItem = input.getItem();
        return true;
    }

    @Override
    public boolean inventoryUpdate(Inventory inventory) {
        ItemStack input = inventory.getStack(0);
        return !(input.getItem() == this.currentItem);
    }

    @Override
    public void finishWork() {
        // Decrement the output
        inventory.getStack(0).decrement(1);
        int slot = getFirstAvailableSlot();
        if (slot == -1) {
            return;
        }
        inventory.setStack(slot, HerboCraft.LOOTS.getRandomTurret(getQualityType()));
    }

    @Override
    public int estimateWork() {
        return (int) (600*(1/getQualityMultiplier()));
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public Class<?> getContainerClass() {
        return GrowthControllerContainer.class;
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
        if (inventory != null)
            currentItem = inventory.getStack(0).getItem();
    }

    // region Quality types

    private float getQualityMultiplier() {
        return QualityType.fromInt(world.getBlockState(pos).get(AbstractUpgradableBlock.UPGRADE_LEVEL)).getMultiplier();
    }

    private QualityType getQualityType() {
        // TODO: Remove the hardcoded hell that is this code
        return QualityType.fromInt(world.getBlockState(pos).get(AbstractUpgradableBlock.UPGRADE_LEVEL));
    }

    //endregion

    // region Utils
    private int getFirstAvailableSlot() {
        // We skip the first slot to prevent errors.
        for (short i = 1; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    private boolean isAccepted(ItemStack item) {
        // We only check if the item is a valid choice
        return HerboCraft.BASE_SEEDS.contains(item.getItem());
    }
    // endregion
}
