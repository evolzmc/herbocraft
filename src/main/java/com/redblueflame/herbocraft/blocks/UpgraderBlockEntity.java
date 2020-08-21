package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.utils.ComponentsHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

import java.util.Optional;

public class UpgraderBlockEntity extends AbstractProgressBlockEntity {
    public BaseInventory inventory;
    public ItemStack currentItem;

    public UpgraderBlockEntity() {
        super(HerboCraft.UPGRADER_BLOCK_ENTITY);
    }

    @Override
    public boolean checkWork() {
        if (inventory.getStack(10).isEmpty()) {
            int input = getFirstInputSlot();
            if (input == -1) {
                return false;
            }
            ItemStack item = inventory.getStack(input);
            inventory.removeStack(input);
            inventory.setStack(10, item);
            // Manually reset work, as the block was moved
            resetWork();
        }
        ItemStack item = inventory.getStack(10);
        if (getFirstAvailableSlot() == -1) {
            return false;
        }
        if (!isAccepted(item)) {
            return false;
        }
        currentItem = item;
        return true;
    }

    @Override
    public void finishWork() {
        if (inventory.getStack(10).isEmpty()) {
            return;
        }
        ItemStack item = inventory.getStack(10);
        Optional<LevelComponent> comp_opt = ComponentsHandler.getOptionalItemComponent(item);
        if (!comp_opt.isPresent()) {
            // The component is not present, we do nothing.
            resetWork();
            return;
        }
        LevelComponent comp = comp_opt.get();
        comp.addLevels(1);
        comp.setStability(comp.getStability()-25);
        ComponentsHandler.saveItemComponent(item, comp);

        // Move out the item if it's fullt upgraded
        if (comp_opt.get().getStability() <= 25) {
            // Move the item to an output spot
            int target = getFirstAvailableSlot();
            if (target == -1) {
                resetWork();
            }
            inventory.removeStack(10);
            inventory.setStack(target, item);
        }
    }

    @Override
    public int estimateWork() {
        return (int) (600*getQualityMultiplier());
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean inventoryUpdate(Inventory inventory) {
        return inventory.getStack(10) != currentItem;
    }

    @Override
    public Class<?> getContainerClass() {
        return UpgraderBlockContainer.class;
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
            currentItem = inventory.getStack(10);
    }

    private boolean isAccepted(ItemStack item) {
        Optional<LevelComponent> comp = ComponentsHandler.getOptionalItemComponent(item);
        return comp.filter(component -> !component.isSterile() && component.getStability() >= 25).isPresent();
    }

    //region Quality

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

    //endregion
}
