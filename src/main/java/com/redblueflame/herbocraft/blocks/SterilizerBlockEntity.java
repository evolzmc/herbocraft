package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.utils.ComponentsHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.BlockView;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

import java.util.Optional;

public class SterilizerBlockEntity extends AbstractProgressBlockEntity {
    private ItemStack currentItem;
    public BaseInventory inventory;

    public SterilizerBlockEntity() {
        super(HerboCraft.STERILIZER_BLOCK_ENTITY);
    }

    @Override
    public boolean checkWork() {
        if (inventory.getStack(0).isEmpty()) {
            return false;
        }
        if (!isAccepted(inventory.getStack(0))) {
            return false;
        }
        currentItem = inventory.getStack(0);
        return true;
    }

    @Override
    public void finishWork() {
        LevelComponent comp = ComponentsHandler.getItemComponent(inventory.getStack(0));
        comp.setSterile(true);
        ComponentsHandler.saveItemComponent(inventory.getStack(0), comp);
    }

    @Override
    public int estimateWork() {
        return 600-(2* ComponentsHandler.getItemComponent(inventory.getStack(0)).getStability());
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean inventoryUpdate(Inventory inventory) {
        return inventory.getStack(0) != currentItem;
    }

    @Override
    public Class<?> getContainerClass() {
        return SterilizerBlockContainer.class;
    }

    private boolean isAccepted(ItemStack item) {
        // We only check if the item is a valid choice
        return HerboCraft.SEEDS.contains(item.getItem())
                && !ComponentsHandler.getItemComponent(item).isSterile();
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
            currentItem = inventory.getStack(0);
    }
}
